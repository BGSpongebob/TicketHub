package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Client;

@ApplicationScoped
public class ClientRepository implements PanacheRepository<Client> {
}
