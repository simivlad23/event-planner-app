package com.internship.eventplanner.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum LocationType {
    ADDRESS("Address");

    private String type;

    LocationType(String type) {
        this.type = type;
    }
}
