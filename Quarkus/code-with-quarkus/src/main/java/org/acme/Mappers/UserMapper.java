package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.DTOs.OrganizerDTO;
import org.acme.DTOs.SellerDTO;
import org.acme.DTOs.UserDTO;
import org.acme.Model.*;
import org.acme.Repositories.RoleRepository;

@ApplicationScoped
public class UserMapper {

    @Inject
    RoleRepository roleRepository;
    @Inject
    SellerMapper sellerMapper;
    @Inject
    OrganizerMapper organizerMapper;

    public User toUser(UserDTO dto) {
        User entity = new User();

        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());

        Role role = roleRepository.findById(dto.getRoleId());
        entity.setRole(role);

        return entity;
    }

    public UserDTO toUserDTO(User entity) {
        UserDTO dto = new UserDTO();

        dto.setId(entity.getId());
        dto.setRoleId(entity.getRole().getId());

        if (entity.getRole().getRole().equals(RoleEnum.ORGANIZER)) {
            OrganizerDTO organizerDTO = organizerMapper.toOrganizerDTO(entity.getOrganizer().getFirst());
            dto.setOrganizer(organizerDTO);
        }
        else if (entity.getRole().getRole().equals(RoleEnum.SELLER)) {
            SellerDTO sellerDTO = sellerMapper.toSellerDTO(entity.getSeller().getFirst());
            dto.setSeller(sellerDTO);
        }

        return dto;
    }
}
