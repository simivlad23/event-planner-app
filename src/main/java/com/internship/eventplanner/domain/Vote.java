package com.internship.eventplanner.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "voteEvent")
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_to_event_id")
    private long id;

    @ManyToOne
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(
        name = "event_id",
        referencedColumnName = "id")
    private Event event;

    @Column(name = "type")
    private String type;

    public Vote() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VoteUserToEvent{" +
            "id=" + id +
            ", user=" + user +
            ", event=" + event +
            ", type='" + type + '\'' +
            '}';
    }
}

