package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.*;
import com.internship.eventplanner.repository.*;
import com.internship.eventplanner.service.dto.VoteDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventPlannerApp.class)
class VoteResourceTest {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private VoteResource rest;
    private MockMvc mocMvc;

    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mocMvc = MockMvcBuilders
            .standaloneSetup(rest)
            .build();

        user = new User();
        user.setLogin("johndoe");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("johndoe@localhost");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("dummy");

        userRepository.save(user);

        UserGroup userGroup = new UserGroup();
        userGroup.setName("Group Name");
        userGroup.setUsers(null);
        userGroup.setEvents(null);
        userGroupRepository.save(userGroup);

        EventCategory eventCategory = new EventCategory();
        eventCategory.setName("Category");
        eventCategoryRepository.save(eventCategory);

        event = new Event();
        event.setTitle("Title");
        event.setDescription("Description of the event");
        event.setLocationType(LocationType.ADDRESS);
        event.setLocation("Location");
        event.setDateTime(ZonedDateTime.of(2019, 07, 20, 12, 0, 0, 0, ZoneId.of("Europe/Paris")));
        event.setUserGroup(userGroup);
        event.setEventCategory(eventCategory);

        eventRepository.save(event);
    }

    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
        eventRepository.deleteAll();
        eventCategoryRepository.deleteAll();
        userRepository.deleteAll();
        userGroupRepository.deleteAll();
    }

    @Test
    void getVotesForAnEventAndAnUserAndAType() throws Exception {
        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);
        vote.setType("yes");
        voteRepository.save(vote);

        List<Event> events = eventRepository.findAll();

        mocMvc.perform(get("/api/votes/yes/" + events.get(0).getId())
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    void getVotesForAnEventAndAnUser() throws Exception {
        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);
        vote.setType("yes");
        voteRepository.save(vote);

        List<Event> events = eventRepository.findAll();

        mocMvc.perform(get("/api/votes/" + events.get(0).getId())
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }


    @Test
    void saveVote() throws Exception {

        VoteDTO voteDTO = new VoteDTO();
        List<Event> events = eventRepository.findAll();
        List<User> users = userRepository.findAll();

        voteDTO.setEventId(events.get(0).getId());
        voteDTO.setUserId(users.get(0).getId());

        voteDTO.setType("yes");

        mocMvc.perform(post("/api/votes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(voteDTO)))
            .andExpect(status().isCreated());

        List<Vote> votes = voteRepository.findAll();

        assert (votes.get(0).getType().compareTo("yes") == 0);
        assert (votes.get(0).getUser().getId() == voteDTO.getUserId());
        assert (votes.get(0).getEvent().getId() == voteDTO.getEventId());
    }

    @Test
    void getVoteType() throws Exception {
        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);
        vote.setType("yes");
        voteRepository.save(vote);

        List<Event> events = eventRepository.findAll();

        mocMvc.perform(get("/api/my-vote/" + vote.getUser().getId() + "/" + events.get(0).getId())
        )
            .andExpect(status().isOk())
            .andExpect(content().string("yes"));
    }
}
