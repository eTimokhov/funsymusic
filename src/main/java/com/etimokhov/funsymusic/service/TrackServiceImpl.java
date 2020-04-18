package com.etimokhov.funsymusic.service;

import com.etimokhov.funsymusic.model.Track;
import com.etimokhov.funsymusic.repository.TrackRepository;
import org.springframework.stereotype.Service;

@Service
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track getTrack(Long id) {
        return trackRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Track " + id + " not found"));
    }
}
