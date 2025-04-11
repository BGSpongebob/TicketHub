package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.SalesTickets;

@ApplicationScoped
public class SalesTicketsRepository implements PanacheRepository<SalesTickets> {
}
