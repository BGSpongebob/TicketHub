package org.acme.Resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.EventDTO;
import org.acme.Services.EventService;
import org.acme.Tools.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @Inject
    EventService eventService;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Create a new event
    @POST
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/create")
    public Response createEvent(EventDTO eventDTO) {
        String username = jwt.getName();
        String msg;
        try {
            EventDTO createdEvent = eventService.create(eventDTO);
            msg = "Event created with ID: "+createdEvent.getId();
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.CREATED)
                    .entity(createdEvent)
                    .build();
        } catch (Exception e) {
            msg = "Unexpected error while creating event: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Update an existing event
    @PUT
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/update")
    public Response updateEvent(EventDTO eventDTO) {
        String username = jwt.getName();
        String msg;
        try {
            EventDTO updatedEvent = eventService.update(eventDTO);
            msg = "Event updated with ID: "+updatedEvent.getId();
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.ok(updatedEvent).build();
        } catch (Exception e) {
            msg = "Unexpected error while updating event: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve all events
    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/getAll")
    public Response getAllEvents() {
        String username = jwt.getName();
        String msg;
        try {
            List<EventDTO> events = eventService.getAllEvents();
            return Response.ok(events).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving events: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve events by organizer ID
    @GET
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/getByOrganizerId/{organizerId}")
    public Response getEventsByOrganizerId(@PathParam("organizerId") Long organizerId) {
        String username = jwt.getName();
        String msg;
        try {
            List<EventDTO> events = eventService.getEventsByOrganizerId(organizerId);
            return Response.ok(events).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving events by organizer: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve events by seller ID
    @GET
    @RolesAllowed({"SELLER", "ADMIN"})
    @Path("/getBySellerId/{sellerId}")
    public Response getEventsBySellerId(@PathParam("sellerId") Long sellerId) {
        String username = jwt.getName();
        String msg;
        try {
            List<EventDTO> events = eventService.getEventsBySellerId(sellerId);
            return Response.ok(events).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving events by seller: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}