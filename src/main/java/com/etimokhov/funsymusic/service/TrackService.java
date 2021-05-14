package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.dto.form.TrackForm;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrackService {
    Track getTrack(Long id);

    TrackForm processTrackFileUploading(MultipartFile trackFile) throws CannotSaveFileException;

    Track saveTrack(TrackForm trackForm, User uploadedBy);

    Track saveTrack(TrackForm trackForm, String uploadedByUsername);

    String getMediaFileFullPath(Long trackId);

    List<Track> findAllByUploader(Long userId);

    TrackDto mapToDto(Track track);

    Page<Track> findLastUploaded(Integer page, Integer count);

    Page<Track> findLastUploadedByUploader(Long userId, int page, int size);
}
