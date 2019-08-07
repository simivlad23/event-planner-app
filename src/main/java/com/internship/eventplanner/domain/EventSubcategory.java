package com.internship.eventplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "subcategories")
public class EventSubcategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "eventSubcategoryID")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "eventCategoryID")
    private EventCategory eventCategory;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
        name = "eventSubcategory_event",
        joinColumns = {@JoinColumn(name = "eventSubcategoryID")},
        inverseJoinColumns = {@JoinColumn(name = "id")}
    )
    private Set<Event> events = new HashSet<>();

    public EventSubcategory() {

    }

    public EventSubcategory(String name) {
        this.name = name;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EventSubcategory{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", eventCategory=" + eventCategory +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSubcategory that = (EventSubcategory) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(eventCategory, that.eventCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eventCategory);
    }
}
