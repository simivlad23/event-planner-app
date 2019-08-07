package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUserGroupIdOrderByDateTimeDesc(Long userGroupID);

    List<Event> findAllByDateTimeBetween(ZonedDateTime startDate, ZonedDateTime stopDate);
}
