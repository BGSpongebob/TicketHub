package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Rating;

@ApplicationScoped
public class RatingRepository implements PanacheRepository<Rating> {
}
