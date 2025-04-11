package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTOs.SaleDTO;
import org.acme.DTOs.SaleTicketDTO;
import org.acme.Mappers.SaleMapper;
import org.acme.Model.*;
import org.acme.Repositories.*;

@ApplicationScoped
public class SaleService {

    @Inject
    SaleRepository saleRepository;

    @Inject
    SaleMapper saleMapper;

    @Inject
    SalesTicketsRepository salesTicketsRepository;

    @Inject
    EventRepository eventRepository;

    @Inject
    TicketTypeRepository ticketTypeRepository;
    @Inject
    TicketRepository ticketRepository;

    @Transactional
    public SaleDTO create(SaleDTO saleDTO) {
        Sale sale = saleMapper.toSale(saleDTO);
        saleRepository.persist(sale);

        // Handle sales_tickets
        for (SaleTicketDTO saleTicketDTO : saleDTO.getSalesTickets()) {
            //create ticket
            Ticket ticket = ticketRepository.findById(saleTicketDTO.getTicket().getId());
            int quantity = saleTicketDTO.getQuantity();

            //link ticket to sale
            SalesTickets salesTickets = new SalesTickets();
            salesTickets.setSale(sale);
            salesTickets.setTicket(ticket);
            salesTickets.setQuantity(quantity);
            salesTicketsRepository.persist(salesTickets);

            // Update event seat availability
            Event event = eventRepository.findById(ticket.getEvent().getId());
            TicketType ticketType = ticketTypeRepository.findById(ticket.getTType().getId());

            TicketTypeEnum tTypeEnum = ticketType.getTType();
            switch (tTypeEnum) {
                case BASIC:
                    if (event.getAvBasicSeats() > 0 && event.getAvBasicSeats() >= quantity) {
                        event.setAvBasicSeats(event.getAvBasicSeats() - quantity);
                    } else {
                        throw new IllegalStateException("No available Basic seats for event: " + event.getId());
                    }
                    break;
                case PREMIUM:
                    if (event.getAvPremiumSeats() > 0 && event.getAvPremiumSeats() >= quantity) {
                        event.setAvPremiumSeats(event.getAvPremiumSeats() - quantity);
                    } else {
                        throw new IllegalStateException("No available Premium seats for event: " + event.getId());
                    }
                    break;
                case VIP:
                    if (event.getAvVipSeats() > 0 && event.getAvVipSeats() >= quantity) {
                        event.setAvVipSeats(event.getAvVipSeats() - quantity);
                    } else {
                        throw new IllegalStateException("No available VIP seats for event: " + event.getId());
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown ticket type: " + tTypeEnum);
            }
            eventRepository.persist(event); // Persist the updated event
        }

        saleDTO.setId(sale.getId());

        return saleDTO;
    }
}