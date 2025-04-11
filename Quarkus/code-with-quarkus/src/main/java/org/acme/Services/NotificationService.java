package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTOs.NotificationDTO;
import org.acme.Mappers.NotificationMapper;
import org.acme.Model.*;
import org.acme.Repositories.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class NotificationService {

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    NotificationMapper notificationMapper;

    @Inject
    UserRepository userRepository;

    @Inject
    EventRepository eventRepository;

    @Inject
    SalesTicketsRepository salesTicketsRepository;

    @Inject
    TicketRepository ticketRepository;

    @Inject
    SellersEventsRepository sellersEventsRepository;

    @Transactional
    public void create(String title, String text, Long userId) {
        // Create a new Notification entity
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setText(text);
        notification.setIsRead(false); // Default to unread
        notification.setCreatedAt(LocalDateTime.now());

        // Link to the User
        User user = userRepository.findById(userId);
        notification.setUser(user);

        // Persist the notification
        notificationRepository.persist(notification);
    }

    @Transactional
    public void updateReadStatus(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId);

        // Mark as read
        notification.setIsRead(true);
        notificationRepository.persist(notification);
    }

    public List<NotificationDTO> getByUserId(Long userId) {
        // Retrieve all notifications for the user
        List<Notification> notifications = notificationRepository.find("user.id", userId).list();
        return notifications.stream()
                .map(notificationMapper::toNotificationDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long notificationId) {
        notificationRepository.delete("id", notificationId);
    }

    //method to notify organizers about ticket sales
    @Transactional
    public void notifyOrganizersTicketSales() {
        // Get all organizers
        List<Organizer> organizers = userRepository.find("role.role", RoleEnum.ORGANIZER)
                .list()
                .stream()
                .flatMap(user -> user.getOrganizer().stream())
                .toList();


        for (Organizer organizer : organizers) {
            // Get all events for this organizer
            List<Event> events = eventRepository.find("organizer.id", organizer.getId()).list();

            if (events.isEmpty()) {
                continue; // Skip if no events
            }

            StringBuilder notificationText = new StringBuilder("Ticket Sales Update for Your Events:\n");

            for (Event event : events) {
                // Get all tickets sold for this event
                List<Ticket> tickets = ticketRepository.find("event.id", event.getId()).list();
                List<SalesTickets> salesTickets = salesTicketsRepository.find("ticket in ?1", tickets).list();

                if (salesTickets.isEmpty()) {
                    notificationText.append(String.format("- %s: No tickets sold yet.\n", event.getTitle()));
                    continue;
                }

                // Group tickets by type and count them
                Map<TicketTypeEnum, Long> ticketSalesByType = salesTickets.stream()
                        .map(SalesTickets::getTicket)
                        .collect(Collectors.groupingBy(
                                ticket -> ticket.getTType().getTType(),
                                Collectors.counting()
                        ));

                notificationText.append(String.format("- %s:\n", event.getTitle()));
                ticketSalesByType.forEach((type, count) ->
                        notificationText.append(String.format("  * %s: %d sold\n", type, count)));
            }

            // Create notification
            String title = "Periodic Ticket Sales Report";
            String text = notificationText.toString();
            Long userId = organizer.getUser().getId();

            create(title, text, userId);
        }
    }

    @Transactional
    public void notifyUpcomingEvents() {
        // Calculate the date 3 days from now
        LocalDateTime threeDaysFromNowStart = LocalDateTime.now().plusDays(3)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime threeDaysFromNowEnd = threeDaysFromNowStart
                .withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Event> upcomingEvents = eventRepository.find(
                "eventDateTime >= ?1 and eventDateTime <= ?2",
                threeDaysFromNowStart, threeDaysFromNowEnd).list();

        for (Event event : upcomingEvents) {
            // Calculate total remaining tickets
            int totalTicketsLeft = event.getAvBasicSeats() + event.getAvPremiumSeats() + event.getAvVipSeats();

            // Create notification message
            String title = "Event Reminder: " + event.getTitle();
            String text = "The event '" + event.getTitle() + "' is in 3 days (" +
                    event.getEventDateTime().toLocalDate() + ").\n Tickets left to sell: " +
                    totalTicketsLeft + " (BASIC: " + event.getAvBasicSeats() +
                    ", PREMIUM: " + event.getAvPremiumSeats() + ", VIP: " + event.getAvVipSeats() + ").";

            // Notify the organizer
            Long organizerUserId = event.getOrganizer().getUser().getId();
            create(title, text, organizerUserId);

            // Notify all sellers associated with the event
            List<SellersEvents> sellersEvents = sellersEventsRepository.find("event.id", event.getId()).list();
            for (SellersEvents se : sellersEvents) {
                Long sellerUserId = se.getSeller().getUser().getId();
                create(title, text, sellerUserId);
            }
        }
    }
}