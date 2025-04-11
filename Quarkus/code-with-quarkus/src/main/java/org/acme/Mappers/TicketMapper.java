package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.TicketDTO;
import org.acme.Model.Ticket;
import org.acme.Model.TicketType;
import org.acme.Repositories.TicketTypeRepository;

@ApplicationScoped
public class TicketMapper {
    @Inject
    TicketTypeRepository ticketTypeRepository;

    public Ticket toTicket(TicketDTO dto) {
        Ticket entity = new Ticket();

        entity.setPrice(dto.getPrice());

        TicketType tType = ticketTypeRepository.findById(dto.getTTypeId());
        entity.setTType(tType);


        return entity;
    }

    public TicketDTO toTicketDTO(Ticket entity) {
        TicketDTO dto = new TicketDTO();

        dto.setId(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setTTypeId(entity.getTType().getId());
        dto.setEventId(entity.getEvent().getId());

        return dto;
    }
}
