package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.DTOs.BasicEventDTO;
import org.acme.Model.Event;

@ApplicationScoped
public class BasicEventMapper {
    public BasicEventDTO toBasicEventDTO(Event entity) {
        BasicEventDTO dto = new BasicEventDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setEtypeId(entity.getEType().getId());
        dto.setEventDateTime(entity.getEventDateTime());
        return dto;
    }
}
