package com.internship.eventplanner.service;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.*;
import com.internship.eventplanner.repository.*;
import com.internship.eventplanner.service.dto.VoteCounter;
import com.internship.eventplanner.service.dto.VoteDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@SpringBootTest(classes = EventPlannerApp.class)
class VoteServiceTest {

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
    private VoteService voteService;
    private MockMvc mocMvc;

    private Event event;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mocMvc = MockMvcBuilders
            .standaloneSetup(voteService)
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
    void saveVote() {
        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);

        VoteDTO voteDTO = new VoteDTO();
        List<Event> events = eventRepository.findAll();
        List<User> users = userRepository.findAll();

        voteDTO.setEventId(events.get(0).getId());
        voteDTO.setUserId(users.get(0).getId());


        voteDTO.setType("yes");
        voteService.voteOperation(voteDTO);

        List<Vote> votes = voteRepository.findAll();

        assert (votes.get(0).getType().compareTo(voteDTO.getType()) == 0);
        assert (votes.get(0).getUser().getId() == voteDTO.getUserId());
        assert (votes.get(0).getEvent().getId() == voteDTO.getEventId());

    }

    @Test
    void getAllYesNoMaybeVotes() {
        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);

        VoteDTO voteDTO = new VoteDTO();
        List<Event> events = eventRepository.findAll();
        List<User> users = userRepository.findAll();

        voteDTO.setEventId(events.get(0).getId());
        voteDTO.setUserId(users.get(0).getId());


        voteDTO.setType("yes");
        voteService.voteOperation(voteDTO);

        VoteCounter votes = voteService.getVotes(Integer.parseInt(events.get(0).getId().toString()));

        assert (votes.getYesVotes() == 1);
        assert (votes.getNoVotes() == 0);
        assert (votes.getMaybeVotes() == 0);
    }

    @Test
    void updateVote() {

        VoteDTO voteDTO = new VoteDTO();
        List<Event> events = eventRepository.findAll();
        List<User> users = userRepository.findAll();

        voteDTO.setEventId(events.get(0).getId());
        voteDTO.setUserId(users.get(0).getId());


        voteDTO.setType("yes");
        voteService.voteOperation(voteDTO);

        List<Vote> votes = voteRepository.findAll();

        assert (votes.get(0).getType().compareTo("yes") == 0);
        assert (votes.get(0).getUser().getId() == voteDTO.getUserId());
        assert (votes.get(0).getEvent().getId() == voteDTO.getEventId());
        assert (votes.size() == 1);
        voteDTO.setType("no");
        voteService.voteOperation(voteDTO);

        votes = voteRepository.findAll();

        assert (votes.get(0).getType().compareTo("no") == 0);
        assert (votes.get(0).getUser().getId() == voteDTO.getUserId());
        assert (votes.get(0).getEvent().getId() == voteDTO.getEventId());
        assert (votes.size() == 1);
    }


    @Test
    void getVoteType() {
        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);

        VoteDTO voteDTO = new VoteDTO();
        List<Event> events = eventRepository.findAll();
        List<User> users = userRepository.findAll();

        voteDTO.setEventId(events.get(0).getId());
        voteDTO.setUserId(users.get(0).getId());


        voteDTO.setType("yes");
        voteService.voteOperation(voteDTO);

        String type = voteService.getVoteTypeForUserAndEvent(Integer.parseInt(user.getId().toString()), Integer.parseInt(events.get(0).getId().toString()));

        assert (type.equals("yes"));

    }
}
