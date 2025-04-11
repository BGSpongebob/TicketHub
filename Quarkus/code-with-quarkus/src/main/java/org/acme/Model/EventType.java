package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity(name="Event_Types")
public class EventType extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventTypeSeq")
    @SequenceGenerator(name = "eventTypeSeq", sequenceName = "event_type_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private EventTypeEnum eType;

    @OneToMany(mappedBy = "eType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events;

    // Public empty constructor
    public EventType() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventTypeEnum getEType() {
        return eType;
    }

    public void setEType(EventTypeEnum eType) {
        this.eType = eType;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    // toString method
    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", eType=" + eType +
                ", events=" + events +
                '}';
    }
}