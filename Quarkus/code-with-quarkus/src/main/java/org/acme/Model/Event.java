package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name="Events")
public class Event extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventSeq")
    @SequenceGenerator(name = "eventSeq", sequenceName = "event_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column
    private int basicSeats;

    @Column
    private int premiumSeats;

    @Column
    private int vipSeats;

    @Column
    private int avBasicSeats;

    @Column
    private int avPremiumSeats;

    @Column
    private int avVipSeats;

    @Column(name = "event_datetime", nullable = false)
    private LocalDateTime eventDateTime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "e_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EventType eType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "organizer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organizer organizer;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellersEvents> sellersEvents;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    // Public empty constructor
    public Event() {}

    // Getters and setters
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

    public int getBasicSeats() {
        return basicSeats;
    }

    public void setBasicSeats(int basicSeats) {
        this.basicSeats = basicSeats;
    }

    public int getPremiumSeats() {
        return premiumSeats;
    }

    public void setPremiumSeats(int premiumSeats) {
        this.premiumSeats = premiumSeats;
    }

    public int getVipSeats() {
        return vipSeats;
    }

    public void setVipSeats(int vipSeats) {
        this.vipSeats = vipSeats;
    }

    public int getAvBasicSeats() {
        return avBasicSeats;
    }

    public void setAvBasicSeats(int avBasicSeats) {
        this.avBasicSeats = avBasicSeats;
    }

    public int getAvPremiumSeats() {
        return avPremiumSeats;
    }

    public void setAvPremiumSeats(int avPremiumSeats) {
        this.avPremiumSeats = avPremiumSeats;
    }

    public int getAvVipSeats() {
        return avVipSeats;
    }

    public void setAvVipSeats(int avVipSeats) {
        this.avVipSeats = avVipSeats;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public EventType getEType() {
        return eType;
    }

    public void setEType(EventType eType) {
        this.eType = eType;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public List<SellersEvents> getSellersEvents() {
        return sellersEvents;
    }

    public void setSellersEvents(List<SellersEvents> sellersEvents) {
        this.sellersEvents = sellersEvents;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    // toString method
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", basicSeats=" + basicSeats +
                ", premiumSeats=" + premiumSeats +
                ", vipSeats=" + vipSeats +
                ", avBasicSeats=" + avBasicSeats +
                ", avPremiumSeats=" + avPremiumSeats +
                ", avVipSeats=" + avVipSeats +
                ", eventDateTime=" + eventDateTime +
                ", eType=" + eType +
                ", organizer=" + organizer +
                ", sellersEvents=" + sellersEvents +
                ", tickets=" + tickets +
                '}';
    }
}
