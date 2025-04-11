package org.acme.DTOs;

public class TicketDTO {
    private Long id;
    private double price;
    private Long ttypeId;
    private Long eventId;

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

    public Long getTTypeId() {
        return ttypeId;
    }

    public void setTTypeId(Long tTypeId) {
        this.ttypeId = tTypeId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}