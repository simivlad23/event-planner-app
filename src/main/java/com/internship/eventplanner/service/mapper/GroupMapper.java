package com.internship.eventplanner.service.mapper;

import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.service.dto.UserDTO;
import com.internship.eventplanner.service.dto.UserGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GroupMapper {

    @Autowired
    private UserMapper userMapper;

    public List<UserGroupDTO> groupsToGroupDTOs(List<UserGroup> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::groupToGroupDTO)
            .collect(Collectors.toList());
    }

    public UserGroupDTO groupToGroupDTO(UserGroup group) {

        UserGroupDTO userGroupDTO = new UserGroupDTO();

        userGroupDTO.setId(group.getId());
        userGroupDTO.setName(group.getName());
        //this.events = group.getEvents();
        Set<UserDTO> userDTOS = group.getUsers().stream()
            .map(user -> userMapper.userToUserDTO(user))
            .collect(Collectors.toSet());

        userGroupDTO.setUsers(userDTOS);

        return userGroupDTO;
    }

    public List<UserGroup> GroupDTOsToGroups(List<UserGroupDTO> userDTOs) {
        return userDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::groupDTOToGroup)
            .collect(Collectors.toList());
    }

    public UserGroup groupDTOToGroup(UserGroupDTO groupDTO) {
        if (groupDTO == null) {
            return null;
        } else {
            UserGroup group = new UserGroup();
            group.setId(groupDTO.getId());
            group.setName(groupDTO.getName());
//            group.setEvents(groupDTO.getEvents());

            Set<User> userList = groupDTO.getUsers().stream()
                .map(userDTO -> userMapper.userDTOToUser(userDTO))
                .collect(Collectors.toSet());

            group.setUsers(userList);

            return group;
        }
    }

}
