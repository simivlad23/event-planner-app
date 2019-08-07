package com.internship.eventplanner.service;

import com.internship.eventplanner.domain.Comment;
import com.internship.eventplanner.domain.Event;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.repository.CommentRepository;
import com.internship.eventplanner.repository.EventRepository;
import com.internship.eventplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;

    private EventRepository eventRepository;

    private UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public boolean addComment(Comment comment) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to add comment: {}", comment);
        }

        Optional<User> actualUser = userRepository.findById(comment.getUser().getId());
        if (!actualUser.isPresent()) {
            return false;
        }
        comment.setUser(actualUser.get());

        Optional<Event> actualEvent = eventRepository.findById(comment.getEvent().getId());
        if (!actualEvent.isPresent()) {
            return false;
        }
        comment.setEvent(actualEvent.get());

        comment.setDateTime(ZonedDateTime.now());

        commentRepository.save(comment);
        return true;
    }

    public List<Comment> findAllByEvent(long eventId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Service request to find comments by event id: {}", eventId);
        }

        List<Comment> comments = new ArrayList<>();
        Optional<Event> actualEvent = eventRepository.findById(eventId);
        if (!actualEvent.isPresent()) {
            return comments;
        }
        comments = commentRepository.findAllByEvent(actualEvent.get());
        Collections.sort(comments, (o1, o2) -> {
            if (o1.getDateTime() == null || o2.getDateTime() == null)
                return 0;
            return o1.getDateTime().compareTo(o2.getDateTime());
        });
        return comments;

    }
}
