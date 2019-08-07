package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.Comment;
import com.internship.eventplanner.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEvent(@Param("event") Event event);
}
