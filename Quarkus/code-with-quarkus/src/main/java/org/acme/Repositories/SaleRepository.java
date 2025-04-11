package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Sale;

@ApplicationScoped
public class SaleRepository implements PanacheRepository<Sale> {
}
