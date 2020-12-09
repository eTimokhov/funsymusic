package com.etimokhov.funsymusic.dto.form;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BlockUserForm {
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date blockUntil;

    private String reason;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBlockUntil() {
        return blockUntil;
    }

    public void setBlockUntil(Date blockUntil) {
        this.blockUntil = blockUntil;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
