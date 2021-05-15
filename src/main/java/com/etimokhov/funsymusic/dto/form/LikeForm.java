package com.etimokhov.funsymusic.dto.form;

import javax.validation.constraints.NotNull;

public class LikeForm {
    @NotNull
    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        SET, UNSET
    }
}
