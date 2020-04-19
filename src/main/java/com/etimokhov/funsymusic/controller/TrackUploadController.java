package com.etimokhov.funsymusic.controller;

import com.etimokhov.funsymusic.dto.TrackDto;
import com.etimokhov.funsymusic.exception.CannotSaveFileException;
import com.etimokhov.funsymusic.service.TrackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class TrackUploadController {
    private final Logger LOG = LoggerFactory.getLogger(TrackUploadController.class);

    private final TrackService trackService;

    public TrackUploadController(TrackService trackService) {
        this.trackService = trackService;
    }

//    @ModelAttribute
//    public void getTrackDto(Model model) {
//        model.addAttribute("track", new TrackDto());
//    }

    @GetMapping("/uploadTrack")
    public String chooseFile() {
        return "selectTrackFile";
    }

    @PostMapping("/uploadTrack")
    public String uploadTrack(@RequestParam MultipartFile file, Model model) {
        TrackDto trackDto = trackService.processTrackFileUploading(file);
        model.addAttribute("track", trackDto);
        return "saveTrackForm";
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Cannot save file.")
    @ExceptionHandler(CannotSaveFileException.class)
    public String handleException(CannotSaveFileException e, Model model) {
        LOG.warn("Cannot save file", e);
        model.addAttribute("errorMessage", "Cannot save uploaded file. It's invalid or there are some problems with server.");
        return "customErrorPage";
    }

    @PostMapping("/uploadTrack/save")
    public String uploadTrack(@Valid @ModelAttribute("track") TrackDto trackDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "saveTrackForm";
        }
        LOG.info("TRYING TO SAVE VALID TRACK: {}", trackDto);
        return "redirect:/home";
    }
}
