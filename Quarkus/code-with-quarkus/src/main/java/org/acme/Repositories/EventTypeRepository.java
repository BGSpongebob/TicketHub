package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.EventType;

@ApplicationScoped
public class EventTypeRepository implements PanacheRepository<EventType> {
}
