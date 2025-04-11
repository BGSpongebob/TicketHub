package org.acme.Tools;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.Services.NotificationService;

@ApplicationScoped
public class NotificationScheduler {
    @Inject
    NotificationService notificationService;

    // Schedule to run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void sendDailyNotifications() {
        notificationService.notifyOrganizersTicketSales();
        notificationService.notifyUpcomingEvents();
    }
}
