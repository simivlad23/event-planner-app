package com.internship.eventplanner.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.Authority;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.UserGroupRepository;
import com.internship.eventplanner.repository.UserRepository;
import com.internship.eventplanner.security.AuthoritiesConstants;
import com.internship.eventplanner.service.MailService;
import com.internship.eventplanner.service.UserService;
import com.internship.eventplanner.service.dto.UserDTO;
import com.internship.eventplanner.service.dto.UserGroupDTO;
import com.internship.eventplanner.web.rest.errors.ExceptionTranslator;
import com.internship.eventplanner.web.rest.vm.LoginVM;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
class UserGroupResourceTest {
    @Autowired
    AccountResource accountResource;
    UserJWTController userJWTController;
    @Mock
    private UserService mockUserService;
    @Autowired
    private ExceptionTranslator exceptionTranslator;
    @Mock
    private MailService mockMailService;
    private MockMvc restUserMockMvc;

    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupResource rest;
    private MockMvc mocMvc;
    private UserGroup userGroup;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        userGroup = new UserGroup();
        userGroup.setId(1L);
        userGroup.setName("NameTest");
        userGroup.setUsers(new HashSet<>());
        userGroup.setEvents(new HashSet<>());

        MockitoAnnotations.initMocks(this);
        UserGroupResourceTest resource = new UserGroupResourceTest();
        mocMvc = MockMvcBuilders
            .standaloneSetup(rest)
            .build();

        AccountResource accountUserMockResource =
            new AccountResource(userRepository, mockUserService, mockMailService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource)
            .setControllerAdvice(exceptionTranslator)
            .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Ignore
    void create() throws Exception {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        User loggedUser = new User();
        loggedUser.setLogin("test");
        loggedUser.setFirstName("john");
        loggedUser.setLastName("doe");
        loggedUser.setEmail("john.doe@jhipster.com");
        loggedUser.setImageUrl("http://placehold.it/50x50");
        loggedUser.setLangKey("en");
        loggedUser.setAuthorities(authorities);
        loggedUser.setPassword(RandomStringUtils.random(60));



        UserGroupDTO ev = new UserGroupDTO();
        UserDTO user = new UserDTO();
        user.setEmail("email@test.com");
        user.setFirstName("Simion");
        user.setLastName("Vlad");

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(false);
        userJWTController.authorize(loginVM);

        ev.setName("Groupname tset");
        Set<UserDTO> users = new HashSet<>();
        users.add(user);
        ev.setUsers(users);

        mocMvc.perform(MockMvcRequestBuilders
            .post("/api/userGroups")
            .content(asJsonString(ev))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        userGroupRepository.deleteAll();
    }

}
