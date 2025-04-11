package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.SaleTicketDTO;
import org.acme.Model.SalesTickets;

@ApplicationScoped
public class SaleTicketMapper {
    @Inject
    TicketMapper ticketMapper;

    public SaleTicketDTO toSaleTicketDTO(SalesTickets entity) {
        SaleTicketDTO dto = new SaleTicketDTO();

        dto.setId(entity.getId());
        dto.setTicket(ticketMapper.toTicketDTO(entity.getTicket()));
        dto.setQuantity(entity.getQuantity());
        dto.setSaleId(entity.getSale().getId());

        return dto;
    }
}
