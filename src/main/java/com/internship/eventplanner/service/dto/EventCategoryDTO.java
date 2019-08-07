package com.internship.eventplanner.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EventCategoryDTO {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    public EventCategoryDTO(@NotBlank @Size(min = 1, max = 50) String name) {
        this.name = name;
    }

    public EventCategoryDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EventCategoryDTO{" +
            "name='" + name + '\'' +
            '}';
    }
}
