package org.acme.Mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.DTOs.ClientDTO;
import org.acme.Model.Client;

@ApplicationScoped
public class ClientMapper {
    public Client toClient(ClientDTO dto) {
        Client entity = new Client();
        entity.setName1(dto.getName1());
        entity.setName2(dto.getName2());
        entity.setPhone(dto.getPhone());
        return entity;
    }

    public ClientDTO toClientDTO(Client entity) {
        ClientDTO dto = new ClientDTO();
        dto.setId(entity.getId());
        dto.setName1(entity.getName1());
        dto.setName2(entity.getName2());
        dto.setPhone(entity.getPhone());
        return dto;
    }
}
