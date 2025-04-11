package org.acme.Resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.RatingDTO;
import org.acme.Services.RatingService;
import org.acme.Tools.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/ratings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RatingResource {

    @Inject
    RatingService ratingService;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Create a new rating
    @POST
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/create")
    public Response createRating(RatingDTO ratingDTO) {
        String username = jwt.getName();
        String msg;
        try {
            RatingDTO createdRating = ratingService.create(ratingDTO);
            msg = "Rating created with ID: " + createdRating.getId();
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.CREATED)
                    .entity(createdRating)
                    .build();
        } catch (Exception e) {
            msg = "Unexpected error while creating rating: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Update an existing rating
    @PUT
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/update")
    public Response updateRating(RatingDTO ratingDTO) {
        String username = jwt.getName();
        String msg;
        try {
            RatingDTO updatedRating = ratingService.updateRating(ratingDTO);
            msg = "Rating updated with ID: " + updatedRating.getId();
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.ok(updatedRating).build();
        } catch (Exception e) {
            msg = "Unexpected error while updating rating: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve ratings by organizer ID
    @GET
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/getByOrganizer/{organizerId}")
    public Response getRatingsByOrganizerId(@PathParam("organizerId") Long organizerId) {
        String username = jwt.getName();
        String msg;
        try {
            List<RatingDTO> ratings = ratingService.getByOrganizerId(organizerId);
            return Response.ok(ratings).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving ratings: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}