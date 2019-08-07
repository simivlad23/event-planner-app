package com.internship.eventplanner.service;

import com.internship.eventplanner.domain.*;
import com.internship.eventplanner.repository.*;
import com.internship.eventplanner.service.dto.VoteCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class EventService {

    private Logger logger = LoggerFactory.getLogger(EventService.class);

    private EventRepository eventRepository;

    private EventCategoryRepository eventCategoryRepository;

    private UserGroupRepository userGroupRepository;

    private EventSubcategoryRepository eventSubcategoryRepository;

    private NotificationRepository notificationRepository;

    private UserRepository userRepository;

    @Autowired
    private VoteService voteService;

    public EventService(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository,
                        UserGroupRepository userGroupRepository, EventSubcategoryRepository eventSubcategoryRepository,
                        NotificationRepository notificationRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventSubcategoryRepository = eventSubcategoryRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    public Event addEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to add event:{}", event);
        }

        Set<EventSubcategory> list = event.getEventSubcategories();
        Set<EventSubcategory> actualSubcategories = new HashSet<>();
        for (EventSubcategory sub : list) {
            Optional<EventSubcategory> actualSub = eventSubcategoryRepository.findById(sub.getId());
            if (actualSub.isPresent()) {
                actualSubcategories.add(actualSub.get());
                actualSub.get().getEvents().add(event);
            }
        }

        Optional<UserGroup> actualUserGroup = userGroupRepository.findById(event.getUserGroup().getId());
        if (actualUserGroup.isPresent()) {
            event.setUserGroup(actualUserGroup.get());
        }
        Optional<EventCategory> actualEventCategory = eventCategoryRepository.findById(event.getEventCategory().getId());
        if (actualEventCategory.isPresent()) {
            event.setEventCategory(actualEventCategory.get());
        }

        final Set<SubEvent> subEventsSet = new HashSet<>();
        for (SubEvent subEventit : event.getSubEvents()) {

            final SubEvent subEvent = new SubEvent();
            subEvent.setDetails(subEventit.getDetails());
            subEvent.setEvent(event);
            subEvent.setTitle(subEventit.getTitle());
            subEvent.setLat(subEventit.getLat());
            subEvent.setLng(subEventit.getLng());

            subEventsSet.add(subEvent);
        }
        event.setSubEvents(subEventsSet);
        return eventRepository.save(event);
    }

    public void removeEvent(long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to remove event with ID:{}", id);
        }
        eventRepository.deleteById(id);
    }

    @Scheduled(cron = "${schelude.event-notification}")
    public void addNotificationIfEventTakesPlace() {

        getAllEventsForTomorrow().stream().forEach(event -> {

            UserGroup userGroup = userGroupRepository.findUserGroupById(event.getUserGroup().getId());
            userGroup.getUsers().stream().forEach(user -> {
                Notification notification = new Notification();
                notification.setEvent(event);
                notification.setValid(true);
                notification.setRead(false);
                notification.setDateTime(ZonedDateTime.now());
                notification.setType(NotificationType.REMINDER);
                if (eventWillTakePlace(event.getId())) {
                    notification.setMessage("The event " + event.getTitle() + " will take place tomorrow");
                } else {
                    notification.setMessage("The event " + event.getTitle() + " will not take place");
                }
                final Optional<User> systemUser = userRepository.findOneByLogin("system");
                if (systemUser.isPresent()) {
                    notification.setSentBy(systemUser.get());
                    notification.setSentTo(user);
                }
                notificationRepository.save(notification);
            });
        });
    }

    public boolean updateEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to update event:{}", event);
        }

        Optional<Event> optionalEvent = eventRepository.findById(event.getId());
        if(optionalEvent.isPresent()) {
            Event actualEvent = optionalEvent.get();

            actualEvent.setTitle(event.getTitle());
            actualEvent.setDescription(event.getDescription());
            Optional<UserGroup> actualUserGroup = userGroupRepository.findById(event.getUserGroup().getId());
            if (actualUserGroup.isPresent()) {
                actualEvent.setUserGroup(actualUserGroup.get());
            }
            Optional<EventCategory> actualEventCategory = eventCategoryRepository.findById(event.getEventCategory().getId());
            if (actualEventCategory.isPresent()) {
                actualEvent.setEventCategory(actualEventCategory.get());
            }
            actualEvent.setDateTime(event.getDateTime());
            actualEvent.setLocationType(event.getLocationType());
            actualEvent.setLocation(event.getLocation());
            for (EventSubcategory sub : actualEvent.getEventSubcategories()) {
                Optional<EventSubcategory> actualSub = eventSubcategoryRepository.findById(sub.getId());
                actualSub.get().getEvents().remove(actualEvent);
            }
            actualEvent.getEventSubcategories().removeAll(actualEvent.getEventSubcategories());
            for (EventSubcategory sub : event.getEventSubcategories()) {
                Optional<EventSubcategory> actualSub = eventSubcategoryRepository.findById(sub.getId());
                if (actualSub.isPresent()) {
                    event.getEventSubcategories().add(actualSub.get());
                    actualSub.get().getEvents().add(actualEvent);
                }
            }

            actualEvent.getSubEvents().removeAll(actualEvent.getSubEvents());

            final Set<SubEvent> subEventsSet = new HashSet<>();
            for (SubEvent subEventit : event.getSubEvents()) {

                if(actualEvent.getSubEvents().contains(subEventit)) continue;
                final SubEvent subEvent = new SubEvent();
                subEvent.setDetails(subEventit.getDetails());
                subEvent.setEvent(actualEvent);
                subEvent.setTitle(subEventit.getTitle());
                subEvent.setLat(subEventit.getLat());
                subEvent.setLng(subEventit.getLng());

                subEventsSet.add(subEvent);
            }
            actualEvent.getSubEvents().addAll(subEventsSet);

            eventRepository.save(actualEvent);
            return true;
        }

        return false;
    }


    private boolean eventWillTakePlace(long eventId) {
        VoteCounter votes = voteService.getVotes(Integer.parseInt(String.valueOf(eventId)));
        int total = votes.getMaybeVotes();
        total += votes.getNoVotes();
        total += votes.getYesVotes();

        return votes.getYesVotes() > total / 2;
    }

    private List<Event> getAllEventsForTomorrow() {
        ZonedDateTime today = ZonedDateTime.now();
        today = today.truncatedTo(ChronoUnit.DAYS);
        today = today.plusDays(1);
        ZonedDateTime tomorrow = today.plusDays(1);
        return eventRepository.findAllByDateTimeBetween(today, tomorrow);
    }
}
