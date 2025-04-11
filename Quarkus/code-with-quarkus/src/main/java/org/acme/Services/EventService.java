package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTOs.EventDTO;
import org.acme.DTOs.SellerDTO;
import org.acme.DTOs.TicketDTO;
import org.acme.Mappers.EventMapper;
import org.acme.Mappers.TicketMapper;
import org.acme.Model.Event;
import org.acme.Model.Seller;
import org.acme.Model.SellersEvents;
import org.acme.Model.Ticket;
import org.acme.Repositories.EventRepository;
import org.acme.Repositories.SellerRepository;
import org.acme.Repositories.SellersEventsRepository;
import org.acme.Repositories.TicketRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class EventService {

    @Inject
    EventRepository eventRepository;

    @Inject
    EventMapper eventMapper;

    @Inject
    SellerRepository sellerRepository;

    @Inject
    SellersEventsRepository sellersEventsRepository;

    @Inject
    NotificationService notificationService;

    @Inject
    TicketRepository ticketRepository;

    @Inject
    TicketMapper ticketMapper;

    @Transactional
    public EventDTO create(EventDTO eventDTO) {
        Event event = eventMapper.toEvent(eventDTO);
        eventRepository.persist(event);

        // Handle sellers and notifications
        for (SellerDTO sellerDTO : eventDTO.getSellers()) {
            Seller seller = sellerRepository.findById(sellerDTO.getId());

            // Create a SellersEvents entry
            SellersEvents sellersEvents = new SellersEvents();
            sellersEvents.setEvent(event);
            sellersEvents.setSeller(seller);
            sellersEventsRepository.persist(sellersEvents);

            // Create a notification for the seller using the user ID from the Seller
            Long userId = seller.getUser().getId();
            String title = "Selected for Event";
            String text = "You have been selected to sell tickets for the event: " + event.getTitle();
            notificationService.create(title, text, userId);
        }

        //Handle tickets
        for (TicketDTO ticketDTO : eventDTO.getTickets()) {
            //Create a ticket entry
            Ticket ticket = ticketMapper.toTicket(ticketDTO);
            ticket.setEvent(event);
            ticketRepository.persist(ticket);
            ticketDTO.setId(ticket.getId());
        }

        eventDTO.setId(event.getId());

        return eventDTO;
    }

    @Transactional
    public EventDTO update(EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(eventDTO.getId());

        existingEvent.setTitle(eventDTO.getTitle());
        existingEvent.setDescription(eventDTO.getDescription());
        existingEvent.setBasicSeats(eventDTO.getBasicSeats());
        existingEvent.setPremiumSeats(eventDTO.getPremiumSeats());
        existingEvent.setVipSeats(eventDTO.getVipSeats());
        existingEvent.setAvBasicSeats(eventDTO.getAvBasicSeats());
        existingEvent.setAvPremiumSeats(eventDTO.getAvPremiumSeats());
        existingEvent.setAvVipSeats(eventDTO.getAvVipSeats());
        existingEvent.setEventDateTime(eventDTO.getEventDateTime());

        // Persist the updated event
        eventRepository.persist(existingEvent);

        // Handle sellers updates
        // Get current seller IDs from SellersEvents
        List<SellersEvents> currentSellersEvents = sellersEventsRepository.find("event.id", eventDTO.getId()).list();
        Set<Long> currentSellerIds = currentSellersEvents.stream()
                .map(se -> se.getSeller().getId())
                .collect(Collectors.toSet());


        // Get new seller IDs from DTO
        Set<Long> newSellerIds = eventDTO.getSellers().stream()
                .map(SellerDTO::getId)
                .collect(Collectors.toSet());

        // Remove sellers no longer in the list
        for (SellersEvents se : currentSellersEvents) {
            if (!newSellerIds.contains(se.getSeller().getId())) {
                sellersEventsRepository.delete("id",se.getId());

                // Create a notification for removal
                Long userId = se.getSeller().getUser().getId();
                String title = "Removed from Event";
                String text = "You have been removed from the seller list for the event: " + eventDTO.getTitle();
                notificationService.create(title, text, userId);
            }
        }

        // Add new sellers and send notifications
        for (Long sellerId : newSellerIds) {
            if (!currentSellerIds.contains(sellerId)) {
                Seller seller = sellerRepository.findById(sellerId);
                SellersEvents sellersEvents = new SellersEvents();
                sellersEvents.setEvent(existingEvent);
                sellersEvents.setSeller(seller);
                sellersEventsRepository.persist(sellersEvents);

                Long userId = seller.getUser().getId();
                String title = "Selected for Event";
                String text = "You have been selected to sell tickets for the event: " + existingEvent.getTitle();
                notificationService.create(title, text, userId);
            }
        }

        return eventDTO;
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.listAll().stream()
                .map(eventMapper::toEventDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByOrganizerId(Long organizerId) {
        return eventRepository.find("organizer.id", organizerId).list().stream()
                .map(eventMapper::toEventDTO)
                .collect(Collectors.toList());
    }

    public List<EventDTO> getEventsBySellerId(Long sellerId) {
        // Retrieve all SellersEvents entries for the seller, then get the events
        List<SellersEvents> sellersEvents = sellersEventsRepository.find("seller.id", sellerId).list();
        return sellersEvents.stream()
                .map(SellersEvents::getEvent)
                .map(eventMapper::toEventDTO)
                .collect(Collectors.toList());
    }
}