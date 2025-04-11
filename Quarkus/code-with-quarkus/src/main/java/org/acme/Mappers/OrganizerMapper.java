package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.DTOs.OrganizerDTO;
import org.acme.Model.Organizer;

@ApplicationScoped
public class OrganizerMapper {
    public Organizer toOrganizer(OrganizerDTO dto) {
        Organizer entity = new Organizer();

        entity.setName1(dto.getName1());
        entity.setName2(dto.getName2());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());

        return entity;
    }

    public OrganizerDTO toOrganizerDTO(Organizer entity) {
        OrganizerDTO dto = new OrganizerDTO();

        dto.setId(entity.getId());
        dto.setName1(entity.getName1());
        dto.setName2(entity.getName2());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());

        return dto;
    }
}
