package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.DTOs.NotificationDTO;
import org.acme.Model.Notification;

@ApplicationScoped
public class NotificationMapper {
    public NotificationDTO toNotificationDTO(Notification entity) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setText(entity.getText());
        dto.setIsRead(entity.getIsRead());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUserId(entity.getUser().getId());
        return dto;
    }
}
