package com.internship.eventplanner.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VoteDTO {

    @NotNull
    private long userId;

    @NotNull
    private long eventId;

    @NotBlank
    private String type;

    public VoteDTO() {
    }

    public VoteDTO(@NotBlank long userId, @NotBlank long eventId, @NotBlank String type) {
        this.userId = userId;
        this.eventId = eventId;
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
