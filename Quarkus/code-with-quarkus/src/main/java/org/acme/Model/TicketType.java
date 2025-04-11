package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity(name="Ticket_Types")
public class TicketType extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketTypeSeq")
    @SequenceGenerator(name = "ticketTypeSeq", sequenceName = "ticket_type_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private TicketTypeEnum tType;

    @OneToMany(mappedBy = "tType", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    // Public empty constructor
    public TicketType() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketTypeEnum getTType() {
        return tType;
    }

    public void setTType(TicketTypeEnum tType) {
        this.tType = tType;
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
        return "TicketType{" +
                "id=" + id +
                ", tType=" + tType +
                ", tickets=" + tickets +
                '}';
    }
}
