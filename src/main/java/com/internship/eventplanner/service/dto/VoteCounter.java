package com.internship.eventplanner.service.dto;

public class VoteCounter {
    private int yesVotes;
    private int noVotes;
    private int maybeVotes;

    public VoteCounter() {
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public void setYesVotes(int yesVotes) {
        this.yesVotes = yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(int noVotes) {
        this.noVotes = noVotes;
    }

    public int getMaybeVotes() {
        return maybeVotes;
    }

    public void setMaybeVotes(int maybeVotes) {
        this.maybeVotes = maybeVotes;
    }

    @Override
    public String toString() {
        return "VoteCounter{" +
            "yesVotes=" + yesVotes +
            ", noVotes=" + noVotes +
            ", maybeVotes=" + maybeVotes +
            '}';
    }
}
