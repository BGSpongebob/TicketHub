package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.time.LocalDate;
import java.util.List;

@Entity(name="Sales")
public class Sale extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "saleSeq")
    @SequenceGenerator(name = "saleSeq", sequenceName = "sale_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private LocalDate saleDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "seller_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Seller seller;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesTickets> salesTickets;

    // Public empty constructor
    public Sale() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return saleDate;
    }

    public void setDate(LocalDate date) {
        this.saleDate = date;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
        return "Sale{" +
                "id=" + id +
                ", date=" + saleDate +
                ", seller=" + seller +
                ", client=" + client +
                ", salesTickets=" + salesTickets +
                '}';
    }
}