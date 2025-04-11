package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.EventDTO;
import org.acme.Model.*;
import org.acme.Repositories.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventMapper {
    @Inject
    EventTypeRepository eventTypeRepository;

    @Inject
    OrganizerRepository organizerRepository;

    @Inject
    OrganizerMapper organizerMapper;

    @Inject
    SellerMapper sellerMapper;

    @Inject
    TicketMapper ticketMapper;

    public Event toEvent(EventDTO dto) {
        Event entity = new Event();

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setBasicSeats(dto.getBasicSeats());
        entity.setPremiumSeats(dto.getPremiumSeats());
        entity.setVipSeats(dto.getVipSeats());
        entity.setAvBasicSeats(dto.getAvBasicSeats());
        entity.setAvPremiumSeats(dto.getAvPremiumSeats());
        entity.setAvVipSeats(dto.getAvVipSeats());
        entity.setEventDateTime(dto.getEventDateTime());

        EventType eType = eventTypeRepository.findById(dto.getETypeId());
        entity.setEType(eType);

        Organizer organizer = organizerRepository.findById(dto.getOrganizer().getId());
        entity.setOrganizer(organizer);

        return entity;
    }

    public EventDTO toEventDTO(Event entity) {
        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setBasicSeats(entity.getBasicSeats());
        dto.setPremiumSeats(entity.getPremiumSeats());
        dto.setVipSeats(entity.getVipSeats());
        dto.setAvBasicSeats(entity.getAvBasicSeats());
        dto.setAvPremiumSeats(entity.getAvPremiumSeats());
        dto.setAvVipSeats(entity.getAvVipSeats());
        dto.setEventDateTime(entity.getEventDateTime());
        dto.setETypeId(entity.getEType().getId());

        dto.setOrganizer(organizerMapper.toOrganizerDTO(entity.getOrganizer()));

        if (entity.getSellersEvents() != null) {
            dto.setSellers(entity.getSellersEvents().stream()
                    .map(se -> sellerMapper.toSellerDTO(se.getSeller()))
                    .collect(Collectors.toList()));
        }

        if (entity.getTickets() != null) {
            dto.setTickets(entity.getTickets().stream()
                    .map(t -> ticketMapper.toTicketDTO(t))
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
