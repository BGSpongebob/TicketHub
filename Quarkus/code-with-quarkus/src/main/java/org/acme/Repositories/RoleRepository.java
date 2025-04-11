package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Role;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {
}
