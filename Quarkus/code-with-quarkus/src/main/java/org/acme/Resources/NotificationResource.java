package org.acme.Resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.NotificationDTO;
import org.acme.Services.NotificationService;
import org.acme.Tools.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    NotificationService notificationService;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Create a new notification
    @POST
    @RolesAllowed({"ORGANIZER", "ADMIN"})
    @Path("/create")
    public Response createNotification(NotificationDTO notificationDTO) {
        String msg;
        try {
            // Since NotificationService.create takes title, text, and userId, map from DTO
            notificationService.create(notificationDTO.getTitle(), notificationDTO.getText(), notificationDTO.getUserId());
            msg = "Notification created with title: " + notificationDTO.getTitle();
            logger.info(msg, this.getClass().getName(), "SERVER_OPERATION");
            
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            msg = "Unexpected error while creating notification: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), "SERVER_OPERATION");
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Update notification read status
    @PUT
    @RolesAllowed({"ORGANIZER", "SELLER", "ADMIN"})
    @Path("/read/{id}")
    public Response updateReadStatus(@PathParam("id") Long id) {
        String username = jwt.getName();
        String msg;
        try {
            notificationService.updateReadStatus(id);
            msg = "Notification read with ID: " + id;
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.ok().build();
        } catch (Exception e) {
            msg = "Unexpected error while reading notification: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Retrieve notifications by user ID
    @GET
    @RolesAllowed({"ORGANIZER", "SELLER", "ADMIN"})
    @Path("/getByUserId/{userId}")
    public Response getNotificationsByUserId(@PathParam("userId") Long userId) {
        String username = jwt.getName();
        String msg;
        try {
            List<NotificationDTO> notifications = notificationService.getByUserId(userId);
            return Response.ok(notifications).build();
        } catch (Exception e) {
            msg = "Unexpected error while retrieving notifications: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }

    // Delete a notification
    @DELETE
    @RolesAllowed({"ORGANIZER", "SELLER", "ADMIN"})
    @Path("delete/{id}")
    public Response deleteNotification(@PathParam("id") Long id) {
        String username = jwt.getName();
        String msg;
        try {
            notificationService.delete(id);
            msg = "Notification deleted with ID: " + id;
            logger.info(msg, this.getClass().getName(), username);
            
            return Response.noContent().build();
        } catch (Exception e) {
            msg = "Unexpected error while deleting notification: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}