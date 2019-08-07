package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.SubEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubEventRepository extends JpaRepository<SubEvent, Long> {
}
