package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.SellerDTO;
import org.acme.DTOs.SellerQueryDTO;
import org.acme.Mappers.SellerMapper;
import org.acme.Mappers.SellerQueryMapper;
import org.acme.Repositories.SellerRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SellerService {

    @Inject
    SellerRepository sellerRepository;

    @Inject
    SellerMapper sellerMapper;

    @Inject
    SellerQueryMapper sellerQueryMapper;

    public List<SellerDTO> getAllSellers() {
        return sellerRepository.listAll().stream()
                .map(sellerMapper::toSellerDTO)
                .collect(Collectors.toList());
    }

    public List<SellerQueryDTO> getAllSellersWithSales() {
        return sellerRepository.listAll().stream()
                .map(sellerQueryMapper::toSellerQueryDTO)
                .collect(Collectors.toList());
    }
}