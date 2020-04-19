package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.repository.TrackRepository;
import com.etimokhov.funsymusic.util.MediaFileSaver;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TrackServiceImpl implements TrackService {
    private final Logger LOG = LoggerFactory.getLogger(TrackServiceImpl.class);

    private final MediaFileSaver mediaFileSaver;

    private final TrackRepository trackRepository;

    public TrackServiceImpl(MediaFileSaver mediaFileSaver, TrackRepository trackRepository) {
        this.mediaFileSaver = mediaFileSaver;
        this.trackRepository = trackRepository;
    }

    @Override
    public Track getTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Track " + id + " not found"));
    }

    /**
     * @param trackFile uploaded file
     * @return form for further fulfilling
     * @throws IllegalArgumentException if file is invalid
     */
    @Override
    public TrackDto processTrackFileUploading(MultipartFile trackFile) throws CannotSaveFileException {
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
            mp3FileName = mediaFileSaver.saveMp3(trackFile.getBytes());
        } catch (IOException e) {
            throw new CannotSaveFileException("Cannot save mp3 file.", e);
        }
        TrackDto trackDto = new TrackDto();
        trackDto.setName(FilenameUtils.getBaseName(fileName));
        trackDto.setArtist("Random_artist");
        trackDto.setMediaFileName(mp3FileName);
        trackDto.setLength(165);
        return trackDto;
    }
}
