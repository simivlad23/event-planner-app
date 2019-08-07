package com.internship.eventplanner.domain;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table
public class Notification implements Serializable {

    public Notification() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    private NotificationType type;

    @Column(name = "isValid")
    @ColumnDefault("true")
    private Boolean isValid;

    @ManyToOne
    @JoinColumn(name = "sentBy")
    private User sentBy;

    @ManyToOne
    @JoinColumn(name = "sentTo")
    private User sentTo;

    @Column(name = "dateTime")
    private ZonedDateTime dateTime;

    @Column(name = "isRead")
    @ColumnDefault("false")
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "userGroup_id",
        referencedColumnName = "userGroupID"
    )
    private UserGroup userGroup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "event_id",
        referencedColumnName = "id"
    )
    private Event event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }

    public User getSentTo() {
        return sentTo;
    }

    public void setSentTo(User sentTo) {
        this.sentTo = sentTo;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
