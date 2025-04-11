package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Ticket;

@ApplicationScoped
public class TicketRepository implements PanacheRepository<Ticket> {
}
