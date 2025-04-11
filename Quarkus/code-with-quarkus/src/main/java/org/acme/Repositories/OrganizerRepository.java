package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Organizer;

@ApplicationScoped
public class OrganizerRepository implements PanacheRepository<Organizer> {
}
