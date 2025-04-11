package org.acme.Resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.DTOs.TokenResponse;
import org.acme.DTOs.UserDTO;
import org.acme.Repositories.UserRepository;
import org.acme.Services.UserService;
import org.acme.Tools.Logger;
import org.acme.Tools.TokenGenerator;
import org.acme.Tools.Validation.DuplicateException;
import org.acme.Tools.Validation.DuplicateValidator;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    DuplicateValidator validator;

    @Inject
    TokenGenerator tokenGenerator;

    @Inject
    UserRepository userRepository;

    @Inject
    Logger logger;

    @Inject
    JsonWebToken jwt;

    // Register a new user
    @POST
    @Path("/register")
    public Response registerUser(UserDTO userDTO) {
        String username = jwt.getName();
        String msg;
        try {
            UserDTO registeredUser = userService.register(userDTO);
            msg = "User registered with ID: " + registeredUser.getId();
            logger.info(msg, this.getClass().getName(), registeredUser.getUsername());
            
            return Response.status(Response.Status.CREATED)
                    .entity(registeredUser)
                    .build();
        } catch (Exception e) {
            try {
                validator.validateDuplicate(e);
            } catch (DuplicateException dpe) {
                msg = "Duplicate error while registering user: " + dpe.getMessage();
                logger.error(msg, this.getClass().getName(), username);

                return Response.status(Response.Status.CONFLICT)
                        .entity(msg)
                        .build();
            }
            msg = "Unexpected error while registering user: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), username);
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }



    // Login a user
    @POST
    @Path("/login")
    public Response loginUser(UserDTO loginDTO) {
        String msg;
        try {
            UserDTO loggedInUser = userService.login(loginDTO);
            // Generate JWT with the user's role
            String token = tokenGenerator.generateToken(
                    loginDTO.getUsername(),
                    userRepository.findById(loggedInUser.getId()).getRole().getRole()
            );

            msg = "User logged in with ID: "+loggedInUser.getId();
            logger.info(msg, this.getClass().getName(), loginDTO.getUsername());
            
            // Return both token and user details
            return Response.ok(new TokenResponse(token, loggedInUser)).build();
        } catch (IllegalArgumentException e) {
            msg = "User failed to log in because of invalid credentials.";
            logger.error(msg, this.getClass().getName(), "SYSTEM_OPERATION");
            
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(msg)
                    .build();
        } catch (Exception e) {
            msg = "Unexpected error while logging in: " + e.getMessage();
            logger.error(msg, this.getClass().getName(), "SYSTEM_OPERATION");
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(msg)
                    .build();
        }
    }
}