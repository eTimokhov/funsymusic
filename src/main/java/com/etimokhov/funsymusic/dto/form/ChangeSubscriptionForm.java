package com.etimokhov.funsymusic.dto.form;

import javax.validation.constraints.NotNull;

public class ChangeSubscriptionForm {
    @NotNull
    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public enum Action {
        SUBSCRIBE, UNSUBSCRIBE
    }
}
