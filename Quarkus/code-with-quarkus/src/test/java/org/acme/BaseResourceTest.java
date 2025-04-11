package org.acme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.acme.DTOs.*;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public abstract class BaseResourceTest {
    protected static TokenResponse sellerToken; // Remove final to allow assignment in setup
    protected static TokenResponse organizerToken;
    protected static TokenResponse adminToken;
    protected static ClientDTO sharedClient;
    protected static EventDTO sharedEvent;
    protected static RatingDTO sharedRating;

    @BeforeAll
    public static void setupSharedUsers() {
        // Only initialize if not already done
        if (sellerToken == null) {
            RestAssured.baseURI = "http://localhost:8080";

            // Register and login seller
            SellerDTO sellerDTO = new SellerDTO();
            sellerDTO.setName1("Shared Seller");
            sellerDTO.setName2("Seller");
            sellerDTO.setEmail(generateUniqueEmail());
            sellerDTO.setPhone(generateUniquePhone());
            sellerToken = loginUser(registerUser(sellerDTO));

            // Register and login organizer
            OrganizerDTO organizerDTO = new OrganizerDTO();
            organizerDTO.setName1("Shared Organizer");
            organizerDTO.setName2("Organizer");
            organizerDTO.setEmail(generateUniqueEmail());
            organizerDTO.setPhone(generateUniquePhone());
            organizerToken = loginUser(registerUser(organizerDTO));

            // Register and login admin
            adminToken = loginUser(registerUser(null));

            createNotification(true); //id 1
            createNotification(false); //id 2

            sharedClient = createClient();

            sharedEvent = createEvent();

            sharedRating = createRating();
        }
    }

    // Utility methods to generate unique fields
    protected static String generateUniqueUsername() {
        return "testuser_" + System.currentTimeMillis();
    }

    protected static String generateUniqueEmail() {
        return "testuser_" + (System.currentTimeMillis()) % 1000000 + "@test.com";
    }

    protected static String generateUniquePhone() {
        return "0899" + (System.currentTimeMillis()) % 1000000;
    }

    protected static String registerUser(Object additionalData) {
        UserDTO userDTO = new UserDTO();
        String username = generateUniqueUsername();
        userDTO.setUsername(username);
        userDTO.setPassword("password123");

        if (additionalData instanceof OrganizerDTO) {
            userDTO.setOrganizer((OrganizerDTO) additionalData);
            userDTO.setRoleId(1L);
        } else if (additionalData instanceof SellerDTO) {
            userDTO.setSeller((SellerDTO) additionalData);
            userDTO.setRoleId(2L);
        }
        else
            userDTO.setRoleId(3L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .post("/api/users/register")
                .then()
                .statusCode(201);

        return username;
    }

    protected static TokenResponse loginUser(String username) {
        UserDTO loginDTO = new UserDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword("password123");

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .post("/api/users/login")
                .then()
                .statusCode(200)
                .extract().as(TokenResponse.class);
    }

    protected static ClientDTO createClient() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName1("SharedClient");
        clientDTO.setName2("SharedClient");
        clientDTO.setPhone(generateUniquePhone());

        return RestAssured.given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(clientDTO)
                .post("/api/clients/create")
                .then()
                .statusCode(201)
                .extract().as(ClientDTO.class);
    }

    protected static EventDTO createEvent() {
        List<SellerDTO> sellers = new ArrayList<>();
        sellers.add(sellerToken.user.getSeller());

        List<TicketDTO> tickets = new ArrayList<>();
        TicketDTO ticket = new TicketDTO();
        ticket.setTTypeId(1L);
        ticket.setPrice(100);
        tickets.add(ticket);


        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Shared Event");
        eventDTO.setDescription("Desc");
        eventDTO.setBasicSeats(100);
        eventDTO.setPremiumSeats(50);
        eventDTO.setVipSeats(20);
        eventDTO.setAvBasicSeats(100);
        eventDTO.setAvPremiumSeats(50);
        eventDTO.setAvVipSeats(20);
        eventDTO.setEventDateTime(LocalDateTime.now().plusDays(1));
        eventDTO.setETypeId(1L);
        eventDTO.setOrganizer(organizerToken.user.getOrganizer());
        eventDTO.setSellers(sellers);
        eventDTO.setTickets(tickets);

        return RestAssured.given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(eventDTO)
                .post("/api/events/create")
                .then()
                .statusCode(201)
                .extract().as(EventDTO.class);
    }

    protected static void createNotification(boolean isOrganizer) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Test Notification");
        notificationDTO.setText("Hello");

        if (isOrganizer)
            notificationDTO.setUserId(organizerToken.user.getId());
        else
            notificationDTO.setUserId(sellerToken.user.getId());

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(notificationDTO)
                .post("/api/notifications/create")
                .then()
                .statusCode(201);
    }

    protected static RatingDTO createRating() {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRating(5);
        ratingDTO.setOrganizerId(organizerToken.user.getOrganizer().getId());
        ratingDTO.setSellerId(sellerToken.user.getSeller().getId());

        return RestAssured.given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(ratingDTO)
                .post("/api/ratings/create")
                .then()
                .statusCode(201)
                .extract().as(RatingDTO.class);
    }
}
