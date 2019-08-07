package com.internship.eventplanner.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "event")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "locationType")
    private LocationType locationType;

    @Column(name = "location")
    private String location;

    @Column(name = "dateTime")
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "eventCategoryID")
    private EventCategory eventCategory;

    @ManyToMany(mappedBy = "events", fetch = FetchType.EAGER)
    private Set<EventSubcategory> eventSubcategories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "userGroupID")
    private UserGroup userGroup;

    @OneToMany(
        mappedBy = "event",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private Set<SubEvent> subEvents = new HashSet<>();

    @OneToMany(mappedBy = "event")
    private Set<Comment> comments = new HashSet<>();

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Event() {
    }

    public Event(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Set<EventSubcategory> getEventSubcategories() {
        return eventSubcategories;
    }

    public void setEventSubcategories(Set<EventSubcategory> eventSubcategories) {
        this.eventSubcategories = eventSubcategories;
    }

    public Set<SubEvent> getSubEvents() {
        return subEvents;
    }

    public void setSubEvents(Set<SubEvent> subEvents) {
        this.subEvents = subEvents;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", locationType=" + locationType +
            ", location='" + location + '\'' +
            ", dateTime=" + dateTime +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
            Objects.equals(title, event.title) &&
            Objects.equals(description, event.description) &&
            locationType == event.locationType &&
            Objects.equals(location, event.location) &&
            Objects.equals(dateTime, event.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, locationType, location, dateTime);
    }


}
