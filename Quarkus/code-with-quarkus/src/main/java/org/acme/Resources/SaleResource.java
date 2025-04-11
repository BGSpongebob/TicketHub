package org.acme.Resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.SaleDTO;
import org.acme.Services.SaleService;
import org.acme.Tools.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/sales")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SaleResource {

    @Inject
    SaleService saleService;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Create a new sale
    @POST
    @RolesAllowed({"SELLER", "ADMIN"})
    @Path("/create")
    public Response createSale(SaleDTO saleDTO) {
        String username = jwt.getName();
        String msg;
        try {
            SaleDTO createdSale = saleService.create(saleDTO);
            msg = "Sale created with ID: "+createdSale.getId();
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.CREATED)
                    .entity(createdSale)
                    .build();
        } catch (Exception e) {
            msg = "Unexpected error while creating sale: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}