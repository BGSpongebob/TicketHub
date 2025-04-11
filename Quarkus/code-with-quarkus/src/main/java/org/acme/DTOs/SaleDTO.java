package org.acme.DTOs;

import java.time.LocalDate;
import java.util.List;

public class SaleDTO {
    private Long id;
    private LocalDate saleDate;
    private Long seller;
    private ClientDTO client;
    private BasicEventDTO event;
    private List<SaleTicketDTO> salesTickets;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public Long getSeller() {
        return seller;
    }

    public void setSeller(Long seller) {
        this.seller = seller;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public BasicEventDTO getEvent() {
        return event;
    }

    public void setEvent(BasicEventDTO event) {
        this.event = event;
    }

    public List<SaleTicketDTO> getSalesTickets() {
        return salesTickets;
    }

    public void setSalesTickets(List<SaleTicketDTO> salesTickets) {
        this.salesTickets = salesTickets;
    }
}
