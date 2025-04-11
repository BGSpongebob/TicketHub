package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.SellerDTO;
import org.acme.DTOs.SellerQueryDTO;
import org.acme.Model.Seller;

import java.util.stream.Collectors;

@ApplicationScoped
public class SellerQueryMapper {
    @Inject
    SellerMapper sellerMapper;
    @Inject
    SaleMapper saleMapper;

    public SellerQueryDTO toSellerQueryDTO(Seller entity) {
        SellerQueryDTO dto = new SellerQueryDTO();

        SellerDTO sellerDTO = sellerMapper.toSellerDTO(entity);
        dto.setSeller(sellerDTO);

        if (entity.getSales() != null) {
            dto.setSales(entity.getSales().stream()
                    .map(saleMapper::toSaleDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
