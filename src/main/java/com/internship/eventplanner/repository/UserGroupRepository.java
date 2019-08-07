package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    UserGroup findUserGroupById(Long id);
    Long countByName(String name);
}
