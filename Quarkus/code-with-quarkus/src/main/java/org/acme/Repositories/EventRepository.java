package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Event;

@ApplicationScoped
public class EventRepository implements PanacheRepository<Event> {
}
