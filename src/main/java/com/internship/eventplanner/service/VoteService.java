package com.internship.eventplanner.service;

import com.internship.eventplanner.domain.Event;
import com.internship.eventplanner.domain.ServiceReturnTypes;
import com.internship.eventplanner.domain.User;
import com.internship.eventplanner.domain.Vote;
import com.internship.eventplanner.repository.VoteRepository;
import com.internship.eventplanner.service.dto.VoteCounter;
import com.internship.eventplanner.service.dto.VoteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public ServiceReturnTypes voteOperation(final VoteDTO voteDTO) {

        Vote vote = voteRepository.findVote(voteDTO.getEventId(), voteDTO.getUserId());
        if (vote == null) {
            vote = createVote(voteDTO);
            voteRepository.save(vote);
            return ServiceReturnTypes.SUCCESS;
        }

        if (vote.getType().equals(voteDTO.getType())) {
            voteRepository.delete(vote);
            return ServiceReturnTypes.DELETED;
        }
        vote.setType(voteDTO.getType());
        voteRepository.save(vote);

        return ServiceReturnTypes.UPDATE;
    }

    private Vote createVote(final VoteDTO voteDTO) {
        final Vote vote = new Vote();

        final Event event = new Event();
        event.setId(voteDTO.getEventId());

        final User user = new User();
        user.setId(voteDTO.getUserId());

        vote.setEvent(event);
        vote.setUser(user);
        vote.setType(voteDTO.getType());
        return vote;
    }

    public VoteCounter getVotes(int eventId) {
        final VoteCounter votes = new VoteCounter();
        votes.setYesVotes(voteRepository.countByEventAndType(eventId, "yes"));
        votes.setNoVotes(voteRepository.countByEventAndType(eventId, "no"));
        votes.setMaybeVotes(voteRepository.countByEventAndType(eventId, "maybe"));

        return votes;
    }

    public String getVoteTypeForUserAndEvent(Integer user_id, Integer event_id) {
        return voteRepository.findType(user_id, event_id);
    }
}
