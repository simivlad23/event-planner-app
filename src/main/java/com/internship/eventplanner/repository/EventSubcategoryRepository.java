package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.EventCategory;
import com.internship.eventplanner.domain.EventSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventSubcategoryRepository extends JpaRepository<EventSubcategory, Long> {

    Optional<EventSubcategory> findByName(@Param("name") String name);

    List<EventSubcategory> findAllByEventCategory(@Param("eventCategory") EventCategory eventCategory);


}
