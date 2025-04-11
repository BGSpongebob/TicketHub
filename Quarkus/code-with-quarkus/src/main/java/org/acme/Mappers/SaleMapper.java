package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.SaleDTO;
import org.acme.Model.Client;
import org.acme.Model.Event;
import org.acme.Model.Sale;
import org.acme.Model.Seller;
import org.acme.Repositories.ClientRepository;
import org.acme.Repositories.EventRepository;
import org.acme.Repositories.SellerRepository;

import java.util.stream.Collectors;

@ApplicationScoped
public class SaleMapper {

    @Inject
    SellerRepository sellerRepository;

    @Inject
    ClientRepository clientRepository;

    @Inject
    BasicEventMapper basicEventMapper;

    @Inject
    ClientMapper clientMapper;

    @Inject
    SaleTicketMapper saleTicketMapper;

    @Inject
    EventRepository eventRepository;

    public Sale toSale(SaleDTO dto) {
        Sale entity = new Sale();

        entity.setDate(dto.getSaleDate());

        Seller seller = sellerRepository.findById(dto.getSeller());
        entity.setSeller(seller);

        Client client = clientRepository.findById(dto.getClient().getId());
        entity.setClient(client);

        return entity;
    }

    public SaleDTO toSaleDTO(Sale entity) {
        SaleDTO dto = new SaleDTO();

        dto.setId(entity.getId());
        dto.setSaleDate(entity.getDate());
        dto.setSeller(entity.getSeller().getId());

        dto.setClient(clientMapper.toClientDTO(entity.getClient()));

        dto.setSalesTickets(entity.getSalesTickets().stream()
                .map(st -> saleTicketMapper.toSaleTicketDTO(st))
                .collect(Collectors.toList()));

        Event event = eventRepository.findById(entity.getSalesTickets().getFirst().getTicket().getEvent().getId());
        dto.setEvent(basicEventMapper.toBasicEventDTO(event));

        return dto;
    }
}
