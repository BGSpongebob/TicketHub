package org.acme.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private int basicSeats;
    private int premiumSeats;
    private int vipSeats;
    private int avBasicSeats;
    private int avPremiumSeats;
    private int avVipSeats;
    private LocalDateTime eventDateTime;
    private Long etypeId;
    private OrganizerDTO organizer;
    private List<SellerDTO> sellers;
    private List<TicketDTO> tickets;

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

    public Long getETypeId() {
        return etypeId;
    }

    public void setETypeId(Long eTypeId) {
        this.etypeId = eTypeId;
    }

    public OrganizerDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerDTO organizer) {
        this.organizer = organizer;
    }

    public List<SellerDTO> getSellers() {
        return sellers;
    }

    public void setSellers(List<SellerDTO> sellers) {
        this.sellers = sellers;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }
}