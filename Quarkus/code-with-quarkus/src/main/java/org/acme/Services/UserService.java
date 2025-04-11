package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTOs.UserDTO;
import org.acme.Mappers.OrganizerMapper;
import org.acme.Mappers.SellerMapper;
import org.acme.Mappers.UserMapper;
import org.acme.Model.Organizer;
import org.acme.Model.Seller;
import org.acme.Model.User;
import org.acme.Repositories.OrganizerRepository;
import org.acme.Repositories.SellerRepository;
import org.acme.Repositories.UserRepository;

import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserMapper userMapper;

    @Inject
    OrganizerMapper organizerMapper;

    @Inject
    OrganizerRepository organizerRepository;

    @Inject
    SellerMapper sellerMapper;

    @Inject
    SellerRepository sellerRepository;

    @Transactional
    public UserDTO register(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        userRepository.persist(user);

        // Handle Organizer if present
        if (userDTO.getOrganizer() != null) {
            Organizer organizer = organizerMapper.toOrganizer(userDTO.getOrganizer());
            organizer.setUser(user); // Link the User to the Organizer
            organizerRepository.persist(organizer);
            userDTO.setOrganizer(organizerMapper.toOrganizerDTO(organizer)); // Update DTO with persisted Organizer
        }

        // Handle Seller if present
        if (userDTO.getSeller() != null) {
            Seller seller = sellerMapper.toSeller(userDTO.getSeller());
            seller.setUser(user); // Link the User to the Seller
            sellerRepository.persist(seller);
            userDTO.setSeller(sellerMapper.toSellerDTO(seller)); // Update DTO with persisted Seller
        }

        userDTO.setId(user.getId());

        return userDTO;
    }

    public UserDTO login(UserDTO loginDTO) {
        // Find user by username
        Optional<User> userOpt = userRepository.find("username", loginDTO.getUsername()).firstResultOptional();
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = userOpt.get();

        // Check password
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Convert to DTO and return
        return userMapper.toUserDTO(user);
    }
}