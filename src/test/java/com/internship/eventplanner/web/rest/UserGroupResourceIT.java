package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.service.UserGroupService;
import com.internship.eventplanner.service.dto.UserIdDTO;
import com.internship.eventplanner.web.rest.errors.ExceptionTranslator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
class UserGroupResourceIT {

    @Autowired
    private AccountResource accountResource;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restUserGroupMockMvc;

    private UserGroup userGroup;

    @BeforeEach
    void setUp() {
        UserGroupResource userGroupResource = new UserGroupResource(userGroupRepository, userGroupService, userRepository, accountResource);

        this.restUserGroupMockMvc = MockMvcBuilders.standaloneSetup(userGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();

        userGroup = new UserGroup();
        userGroup.setName("group1");
    }

    @AfterEach
    void tearDown() {
        userGroupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllUserGroups() throws Exception {
        restUserGroupMockMvc.perform(get("/api/userGroups")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));

        userGroupRepository.save(userGroup);
        restUserGroupMockMvc.perform(get("/api/userGroups")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("group1")));
    }

    @Test
    void getAllUserForGroups() throws Exception {
        restUserGroupMockMvc.perform(get("/api/userGroups/users/255")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        userGroupRepository.save(userGroup);
        userGroup = userGroupRepository.findAll().get(0);
        restUserGroupMockMvc.perform(get("/api/userGroups/users/" + userGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getUserGroupByID() throws Exception {
        restUserGroupMockMvc.perform(get("/api/userGroups/255")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        userGroupRepository.save(userGroup);
        userGroup = userGroupRepository.findAll().get(0);
        restUserGroupMockMvc.perform(get("/api/userGroups/" + userGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("group1")));
    }

    @Test
    void getUsersNotFromGroup() throws Exception {

        userGroupRepository.save(userGroup);
        userGroup = userGroupRepository.findAll().get(0);
        restUserGroupMockMvc.perform(get("/api/userGroups/users/not/" + userGroup.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void addUsersToGroup() throws Exception {

        UserIdDTO usr = new UserIdDTO();

        userGroupRepository.save(userGroup);
        userGroup = userGroupRepository.findAll().get(0);
        restUserGroupMockMvc.perform(put("/api/userGroups/users/add/")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usr)))
            .andExpect(status().isNoContent());

        User user = new User();
        user.setLogin("johndoe");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("dummy");

        userRepository.save(user);

        List<User> users = userRepository.findAll();
        UserIdDTO userid = new UserIdDTO();
        userid.setGroupId(userGroup.getId());
        List<Long> usersId = new ArrayList<>();
        usersId.add(users.get(0).getId());
        userid.setUserId(usersId);
        String result = restUserGroupMockMvc.perform(put("/api/userGroups/users/add/")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userid)))
            .andExpect(status().isInternalServerError()).andReturn().getResolvedException().getMessage();
    }



}
