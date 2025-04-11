package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.TicketType;

@ApplicationScoped
public class TicketTypeRepository implements PanacheRepository<TicketType> {
}
