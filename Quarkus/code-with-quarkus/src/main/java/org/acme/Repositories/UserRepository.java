package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
