package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.domain.Comment;
import com.internship.eventplanner.repository.CommentRepository;
import com.internship.eventplanner.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentResource {
    private final Logger logger = LoggerFactory.getLogger(CommentResource.class);

    private CommentService commentService;

    private CommentRepository commentRepository;

    public CommentResource(CommentService commentService, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/comments/event/{id}")
    public ResponseEntity<List<Comment>> findAllByEvent(@PathVariable("id") long id) {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to find all comments");
        }
        return new ResponseEntity<>(commentService.findAllByEvent(id), HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody Comment comment) throws URISyntaxException {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to create comment:{}", comment);
        }
        boolean wasCreated = commentService.addComment(comment);
        if (wasCreated) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") long id) throws URISyntaxException {
        if (logger.isDebugEnabled()) {
            logger.debug("Rest request to delete comment with id:{}", id);
        }
        commentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
