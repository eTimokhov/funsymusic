package com.etimokhov.funsymusic.dto.form;

public class ChangeSubscriptionForm {
    private String username;
    private Action action;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
