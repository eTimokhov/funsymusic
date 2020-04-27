package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.form.TrackForm;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.exception.NotFoundException;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.util.MediaFileUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        //TODO: parse mp3 file with tag extractor (e.g. Apache Tika);
        // validate it; save media files locally;
        // get meta data (length, name, artist, image);
        // return form for further fulfilling;
        String fileName = trackFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(fileName);
        if (fileExtension == null || !fileExtension.equals("mp3")) {
            throw new CannotSaveFileException("Invalid file extension. Supported extension: mp3");
        }
        String mp3FileName;
        try {
            mp3FileName = mediaFileUtil.saveMp3(trackFile.getBytes());
        } catch (IOException e) {
            throw new CannotSaveFileException("Cannot save mp3 file.", e);
        }
        TrackForm trackForm = new TrackForm();
        trackForm.setName(FilenameUtils.getBaseName(fileName));
        trackForm.setArtist("Random_artist");
        trackForm.setMediaFileName(mp3FileName);
        trackForm.setLength(165);
        return trackForm;
    }

    @Override
    public Track saveTrack(TrackForm trackForm, User uploadedBy) {
        Track track = mapTrackDtoToTrack(trackForm);
        track.setUploader(uploadedBy);
        track = trackRepository.save(track);
        LOG.info("Track {} was successfully saved", track.getId());
        return track;
    }

    @Override
    public String getMediaFileFullPath(Long trackId) {
        Track track = getTrack(trackId);
        return mediaFileUtil.getMediaFileFullPath(track.getMediaFile());
    }

    @Override
    public List<Track> findAllByUploader(Long userId) {
        return trackRepository.findByUploaderId(userId);
    }

    private Track mapTrackDtoToTrack(TrackForm trackForm) {
        return new Track(trackForm.getArtist(), trackForm.getName(),
                trackForm.getMediaFileName(), trackForm.getImageFileName(), trackForm.getLength(), null);
    }
}
