package com.internship.eventplanner.service.dto;

import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.service.mapper.UserMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserGroupDTO {

    private Long id;

    private String name;

    private Set<UserDTO> users = new HashSet<>();

    @Autowired
    private UserMapper userMapper;

    public UserGroupDTO() {
    }

    public UserGroupDTO(UserGroup group) {
        this.id = group.getId();
        this.name = group.getName();
        Set<UserDTO> userDTOS = group.getUsers().stream()
            .map(user -> userMapper.userToUserDTO(user))
            .collect(Collectors.toSet());

        this.users = userDTOS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }
}
