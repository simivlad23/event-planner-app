package com.internship.eventplanner.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum NotificationType {

    GROUP_REQUEST("GROUP_REQUEST"), REMINDER("REMINDER"), INFO("INFO");

    private String type;

    NotificationType(String type){this.type = type;}

    public String getType() {
        return type;
    }
}
