package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.dto.form.TrackForm;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.exception.NotFoundException;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.util.MediaFileUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {
    private final Logger LOG = LoggerFactory.getLogger(TrackServiceImpl.class);

    private final MediaFileUtil mediaFileUtil;

    private final TrackRepository trackRepository;

    public TrackServiceImpl(MediaFileUtil mediaFileUtil, TrackRepository trackRepository) {
        this.mediaFileUtil = mediaFileUtil;
        this.trackRepository = trackRepository;
    }

    @Override
    public Track getTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(() -> new NotFoundException("Track " + id + " not found"));
    }

    /**
     * @param trackFile uploaded file
     * @return form for further fulfilling
     * @throws IllegalArgumentException if file is invalid
     */
    @Override
    public TrackForm processTrackFileUploading(MultipartFile trackFile) throws CannotSaveFileException {
        String fileName = trackFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(fileName);
        if (fileExtension == null || !fileExtension.equals("mp3")) {
            throw new CannotSaveFileException("Invalid file extension. Supported extension: mp3");
        }
        String mp3FileName;
        byte[] mp3FileBytes = new byte[0];
        try {
            mp3FileBytes = trackFile.getBytes();
        } catch (IOException e) {
            throw new CannotSaveFileException(e);
        }
        TrackForm trackForm = new TrackForm();
        fillTrackFormWithMp3Metadata(trackForm, mp3FileBytes);
        try {
            mp3FileName = mediaFileUtil.saveMp3(mp3FileBytes);
        } catch (IOException e) {
            throw new CannotSaveFileException("Cannot save mp3 file.", e);
        }
        trackForm.setMediaFileName(mp3FileName);
        return trackForm;
    }

    private void fillTrackFormWithMp3Metadata(TrackForm trackForm, byte[] mp3File) {
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();

        try (InputStream input = new ByteArrayInputStream(mp3File)) {
            parser.parse(input, handler, metadata, parseCtx);
        } catch (SAXException | TikaException | IOException e) {
            throw new CannotSaveFileException(e);
        }
        String xmPdmDuration = metadata.get(XMPDM.DURATION);
        if (xmPdmDuration == null) {
            throw new CannotSaveFileException("Cannot save file: cannot extract duration.");
        }
        int durationSeconds = (int) (Double.parseDouble(xmPdmDuration) / 1000);
        trackForm.setName(metadata.get(TikaCoreProperties.TITLE));
        trackForm.setArtist(metadata.get(TikaCoreProperties.CREATOR));
        trackForm.setLength(durationSeconds);
    }

    @Override
    public Track saveTrack(TrackForm trackForm, User uploadedBy) {
        Track track = mapTrackDtoToTrack(trackForm);
        track.setUploader(uploadedBy);
        track.setUploadDate(new Date());
        track = trackRepository.save(track);
        LOG.info("Track {} was successfully saved", track.getId());
        return track;
    }

    @Override
    public String getMediaFileFullPath(Long trackId) {
        Track track = getTrack(trackId);
        return mediaFileUtil.getMp3FileFullPath(track.getMediaFile());
    }

    @Override
    public List<Track> findAllByUploader(Long userId) {
        return trackRepository.findByUploaderId(userId);
    }

    @Override
    public TrackDto mapToDto(Track track) {
        return new TrackDto(
                track.getId(),
                track.getName(),
                track.getArtist(),
                track.getLength(),
                track.getMediaFile()
        );
    }

    private Track mapTrackDtoToTrack(TrackForm trackForm) {
        return new Track(trackForm.getArtist(), trackForm.getName(),
                trackForm.getMediaFileName(), trackForm.getLength(), null);
    }
}
