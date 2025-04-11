package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name="Sales_Tickets")
public class SalesTickets extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salesTicketsSeq")
    @SequenceGenerator(name = "salesTicketsSeq", sequenceName = "sales_tickets_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "sale_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ticket_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ticket ticket;

    private int quantity;

    // Public empty constructor
    public SalesTickets() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString method
    @Override
    public String toString() {
        return "SalesTickets{" +
                "id=" + id +
                ", sale=" + sale +
                ", ticket=" + ticket +
                ", quantity=" + quantity +
                '}';
    }
}
