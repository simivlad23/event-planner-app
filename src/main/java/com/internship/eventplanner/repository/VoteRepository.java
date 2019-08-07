package com.internship.eventplanner.repository;

import com.internship.eventplanner.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VoteRepository extends JpaRepository<Vote, Long> {


    @Query("from Vote v where v.event.id = :event_id and v.user.id = :user_id")
    Vote findVote(@Param("event_id") long event_id, @Param("user_id") long user_id);

    @Query("select count(1) from Vote v where v.event.id = :event_id and v.type= :voteType")
    int countByEventAndType(@Param("event_id") long event_id, @Param("voteType") String voteType);

    @Query("select v.type from Vote v where v.user.id = :user_id and v.event.id = :event_id")
    String findType(@Param("user_id") long user_id, @Param("event_id") long event_id);
}
