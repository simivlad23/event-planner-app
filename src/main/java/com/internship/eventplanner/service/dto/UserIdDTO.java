package com.internship.eventplanner.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserIdDTO implements Serializable {
    @NotNull
    private List<Long> userId;
    @NotNull
    private Long groupId;

    public UserIdDTO() {
        userId = new ArrayList<>();
    }

    public List<Long> getUserId() {
        return userId;
    }

    public void setUserId(List<Long> userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
