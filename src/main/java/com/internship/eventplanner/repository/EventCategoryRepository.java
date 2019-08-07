package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {

    EventCategory findByName(@Param("name") String categoryName);
}
