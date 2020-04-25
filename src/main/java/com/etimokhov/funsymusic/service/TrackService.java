package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {
    Track getTrack(Long id);

    TrackDto processTrackFileUploading(MultipartFile trackFile) throws CannotSaveFileException;

    Track saveTrack(TrackDto trackDto, User uploadedBy);

    String getMediaFileFullPath(Long trackId);

    List<Track> findAllByUploader(Long userId);
}
