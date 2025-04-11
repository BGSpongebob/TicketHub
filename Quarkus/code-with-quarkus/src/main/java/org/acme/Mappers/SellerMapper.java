package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.DTOs.SellerDTO;
import org.acme.Model.Seller;

@ApplicationScoped
public class SellerMapper {
    public Seller toSeller(SellerDTO dto) {
        Seller entity = new Seller();

        entity.setName1(dto.getName1());
        entity.setName2(dto.getName2());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setCurrentRating(dto.getCurrentRating());

        return entity;
    }

    public SellerDTO toSellerDTO(Seller entity) {
        SellerDTO dto = new SellerDTO();

        dto.setId(entity.getId());
        dto.setName1(entity.getName1());
        dto.setName2(entity.getName2());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setCurrentRating(entity.getCurrentRating());

        return dto;
    }
}

