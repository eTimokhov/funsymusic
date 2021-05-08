package com.etimokhov.funsymusic.payload.request;

import javax.validation.constraints.NotBlank;

public class SignupRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
