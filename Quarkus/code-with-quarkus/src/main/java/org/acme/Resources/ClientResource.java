package org.acme.Resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.ClientDTO;
import org.acme.Tools.Logger;
import org.acme.Tools.Validation.DuplicateValidator;
import org.acme.Tools.Validation.DuplicateException;
import org.acme.Services.ClientService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @Inject
    ClientService clientService;

    @Inject
    DuplicateValidator validator;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Create a new client
    @POST
    @RolesAllowed({"SELLER", "ADMIN"})
    @Path("/create")
    public Response createClient(ClientDTO clientDTO) {
        String username = jwt.getName();
        String msg;
        try {
            ClientDTO createdClient = clientService.create(clientDTO);
            msg = "Client created with ID: "+createdClient.getId();
            logger.info(msg, this.getClass().getName(), username);

            return Response.status(Response.Status.CREATED)
                    .entity(createdClient)
                    .build();
        } catch (Exception e) {
            try {
                validator.validateDuplicate(e);
            } catch (DuplicateException dpe) {
                msg = "Duplicate error while creating client: "+dpe.getMessage();
                logger.error(msg, this.getClass().getName(), username);

                return Response.status(Response.Status.CONFLICT)
                        .entity(msg)
                        .build();
            }
            msg = "Unexpected error while creating client: "+e.getMessage();
            logger.error(msg, this.getClass().getName(), username);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve all clients
    @GET
    @RolesAllowed({"SELLER", "ADMIN"})
    @Path("/getAll")
    public Response getAllClients() {
        String username = jwt.getName();
        String msg;
        try {
            List<ClientDTO> clients = clientService.getAllClients();
            return Response.ok(clients).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving clients: "+e.getMessage();
            logger.error(msg, this.getClass().getName(), username);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}