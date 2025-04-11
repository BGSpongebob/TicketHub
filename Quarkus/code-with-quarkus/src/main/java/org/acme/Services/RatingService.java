package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTOs.RatingDTO;
import org.acme.Mappers.RatingMapper;
import org.acme.Model.Organizer;
import org.acme.Model.Rating;
import org.acme.Model.Seller;
import org.acme.Repositories.OrganizerRepository;
import org.acme.Repositories.RatingRepository;
import org.acme.Repositories.SellerRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    @Inject
    RatingMapper ratingMapper;

    @Inject
    SellerRepository sellerRepository;

    @Inject
    NotificationService notificationService;

    @Inject
    OrganizerRepository organizerRepository;

    @Transactional
    public RatingDTO create(RatingDTO ratingDTO) {
        Rating rating = ratingMapper.toRating(ratingDTO);
        ratingRepository.persist(rating);
        ratingDTO.setId(rating.getId());

        updateSellerCurrentRating(rating.getSeller().getId());

        //send notification
        Organizer organizer = organizerRepository.findById(ratingDTO.getOrganizerId());
        Long userId = sellerRepository.findById(ratingDTO.getSellerId()).getUser().getId();
        String title = "New Rating";
        String text = "Organizer " + organizer.getName1() + " " + organizer.getName2() + " has given you a rating of " + ratingDTO.getRating();
        notificationService.create(title, text, userId);

        return ratingDTO;
    }

    @Transactional
    public RatingDTO updateRating(RatingDTO ratingDTO) {
        Rating existingRating = ratingRepository.findById(ratingDTO.getId());
        existingRating.setRating(ratingDTO.getRating());

        ratingRepository.persist(existingRating);

        updateSellerCurrentRating(existingRating.getSeller().getId());

        //send notification
        Organizer organizer = organizerRepository.findById(ratingDTO.getOrganizerId());
        Long userId = sellerRepository.findById(ratingDTO.getSellerId()).getUser().getId();
        String title = "Updated Rating";
        String text = "Organizer " + organizer.getName1() + " " + organizer.getName2() + " has updated his rating of you to " + ratingDTO.getRating();
        notificationService.create(title, text, userId);

        return ratingDTO;
    }

    public List<RatingDTO> getByOrganizerId(Long organizerId) {
        List<Rating> ratings = ratingRepository.find("organizer.id", organizerId).list();
        return ratings.stream()
                .map(ratingMapper::toRatingDTO)
                .collect(Collectors.toList());
    }

    // Private helper method to calculate and update the seller's currentRating
    private void updateSellerCurrentRating(Long sellerId) {
        // Fetch all ratings for the seller
        List<Rating> sellerRatings = ratingRepository.find("seller.id", sellerId).list();

        if (!sellerRatings.isEmpty()) {
            // Calculate the average rating
            double sum = sellerRatings.stream()
                    .mapToDouble(Rating::getRating)
                    .sum();
            double average = sum / sellerRatings.size();

            // Round up to one decimal place
            double roundedAverage = Math.round(average * 10.0) / 10.0;

            // Update the seller's currentRating
            Seller seller = sellerRepository.findById(sellerId);
            if (seller != null) {
                seller.setCurrentRating(roundedAverage);
                sellerRepository.persist(seller);
            }
        }
    }
}