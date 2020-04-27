package com.etimokhov.funsymusic.dto.form;

import javax.validation.constraints.NotBlank;

public class PlaylistForm {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
