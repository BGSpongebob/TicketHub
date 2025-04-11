package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.util.List;

@Entity(name="Tickets")
public class Ticket extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketSeq")
    @SequenceGenerator(name = "ticketSeq", sequenceName = "ticket_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "t_type_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TicketType tType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "event_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesTickets> salesTickets;

    // Public empty constructor
    public Ticket() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TicketType getTType() {
        return tType;
    }

    public void setTType(TicketType tType) {
        this.tType = tType;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<SalesTickets> getSalesTickets() {
        return salesTickets;
    }

    public void setSalesTickets(List<SalesTickets> salesTickets) {
        this.salesTickets = salesTickets;
    }

    // toString method
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", price=" + price +
                ", tType=" + tType +
                ", event=" + event +
                ", salesTickets=" + salesTickets +
                '}';
    }
}
