package org.acme.Resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.SellerDTO;
import org.acme.DTOs.SellerQueryDTO;
import org.acme.Services.SellerService;
import org.acme.Tools.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/sellers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SellerResource {

    @Inject
    SellerService sellerService;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Retrieve all sellers
    @GET
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/getAll")
    public Response getAllSellers() {
        String username = jwt.getName();
        String msg;
        try {
            List<SellerDTO> sellers = sellerService.getAllSellers();
            return Response.ok(sellers).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving sellers: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve all sellers with their sales
    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/getAllWithSales")
    public Response getAllSellersWithSales() {
        String username = jwt.getName();
        String msg;
        try {
            List<SellerQueryDTO> sellersWithSales = sellerService.getAllSellersWithSales();
            return Response.ok(sellersWithSales).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving sellers with sales: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}