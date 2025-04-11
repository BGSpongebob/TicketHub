package org.acme.Repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.Model.Seller;

@ApplicationScoped
public class SellerRepository implements PanacheRepository<Seller> {
}
