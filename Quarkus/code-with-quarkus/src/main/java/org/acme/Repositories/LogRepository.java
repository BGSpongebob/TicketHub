package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Log;

@ApplicationScoped
public class LogRepository implements PanacheRepository<Log> {
}
