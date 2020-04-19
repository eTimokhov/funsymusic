package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.model.Track;
import org.springframework.web.multipart.MultipartFile;

public interface TrackService {
    Track getTrack(Long id);

    TrackDto processTrackFileUploading(MultipartFile trackFile) throws CannotSaveFileException;
}
