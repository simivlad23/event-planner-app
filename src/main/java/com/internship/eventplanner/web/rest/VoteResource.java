package com.internship.eventplanner.web.rest;

import com.internship.eventplanner.repository.VoteRepository;
import com.internship.eventplanner.service.VoteService;
import com.internship.eventplanner.service.dto.VoteCounter;
import com.internship.eventplanner.service.dto.VoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VoteResource {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteService voteService;

    @GetMapping("/votes/{type}/{eventId}")
    public ResponseEntity<Integer> getVotesForAnEventAndAnUser(@PathVariable String type, @PathVariable Integer eventId) {

        return new ResponseEntity<>(voteRepository.countByEventAndType(eventId, type), HttpStatus.OK);
    }

    @GetMapping("/my-vote/{user_id}/{event_id}")
    public ResponseEntity<String> getVoteTypeForAnUser(@PathVariable Integer user_id, @PathVariable Integer event_id) {
        return new ResponseEntity<>(voteService.getVoteTypeForUserAndEvent(user_id, event_id), HttpStatus.OK);
    }

    @GetMapping("/votes/{eventId}")
    public ResponseEntity<VoteCounter> getVotesForAnEventAndAnUser(@PathVariable Integer eventId) {

        return new ResponseEntity<>(voteService.getVotes(eventId), HttpStatus.OK);
    }
    @PostMapping("/votes")
    public ResponseEntity<Void> saveVote(@RequestBody VoteDTO voteDTO) {

        voteService.voteOperation(voteDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
