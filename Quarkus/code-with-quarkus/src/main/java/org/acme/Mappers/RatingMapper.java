package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.RatingDTO;
import org.acme.Model.Organizer;
import org.acme.Model.Rating;
import org.acme.Model.Seller;
import org.acme.Repositories.OrganizerRepository;
import org.acme.Repositories.SellerRepository;

@ApplicationScoped
public class RatingMapper {
    @Inject
    SellerRepository sellerRepository;
    @Inject
    OrganizerRepository organizerRepository;

    public Rating toRating(RatingDTO dto) {
        Rating entity = new Rating();

        entity.setRating(dto.getRating());

        Seller seller = sellerRepository.findById(dto.getSellerId());
        entity.setSeller(seller);

        Organizer organizer = organizerRepository.findById(dto.getOrganizerId());
        entity.setOrganizer(organizer);

        return entity;
    }

    public RatingDTO toRatingDTO(Rating entity) {
        RatingDTO dto = new RatingDTO();

        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setSellerId(entity.getSeller().getId());
        dto.setOrganizerId(entity.getOrganizer().getId());

        return dto;
    }
}
