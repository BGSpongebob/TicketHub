package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Notification;

@ApplicationScoped
public class NotificationRepository implements PanacheRepository<Notification> {
}
