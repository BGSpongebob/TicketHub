package org.acme.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.DTOs.ClientDTO;
import org.acme.Mappers.ClientMapper;
import org.acme.Model.Client;
import org.acme.Repositories.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClientService {

    @Inject
    ClientRepository clientRepository;

    @Inject
    ClientMapper clientMapper;

    @Transactional
    public ClientDTO create(ClientDTO clientDTO) {
        Client client = clientMapper.toClient(clientDTO);
        clientRepository.persist(client);
        clientDTO.setId(client.getId());

        return clientDTO;
    }

    public List<ClientDTO> getAllClients() {
        return clientRepository.listAll().stream()
                .map(clientMapper::toClientDTO)
                .collect(Collectors.toList());
    }
}