package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.SellersEvents;

@ApplicationScoped
public class SellersEventsRepository implements PanacheRepository<SellersEvents> {
}
