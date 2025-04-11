package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.DTOs.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class EventResourceTest extends BaseResourceTest {

    @Test
    public void testCreateEventAsOrganizerSuccess() {
        List<SellerDTO> sellers = new ArrayList<>();
        sellers.add(sellerToken.user.getSeller());

        List<TicketDTO> tickets = new ArrayList<>();
        TicketDTO ticket = new TicketDTO();
        ticket.setTTypeId(1L);
        ticket.setPrice(100);
        tickets.add(ticket);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Test Event");
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


        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(eventDTO)
                .post("/api/events/create")
                .then()
                .statusCode(201)
                .body("title", equalTo("Test Event"));
    }

    @Test
    public void testCreateEventInvalidDataFails() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle("Test Event");

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(eventDTO)
                .post("/api/events/create")
                .then()
                .statusCode(500);
    }

    @Test
    public void testCreateEventAsSellerFails() {
        EventDTO eventDTO = new EventDTO();

        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(eventDTO)
                .post("/api/events/create")
                .then()
                .statusCode(403);
    }

    @Test
    public void testUpdateEventAsOrganizerSuccess() {
        sharedEvent.setTitle("Updated Event");

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(sharedEvent)
                .put("/api/events/update")
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Event"));
    }

    @Test
    public void testGetEventsByOrganizerIdSuccess() {
        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .get("/api/events/getByOrganizerId/" + organizerToken.user.getOrganizer().getId())
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testGetEventsBySellerIdSuccess() {
        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .get("/api/events/getBySellerId/" + sellerToken.user.getSeller().getId())
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testGetAllEventsAsAdminSuccess() {
        given()
                .header("Authorization", "Bearer " + adminToken.token)
                .get("/api/events/getAll")
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testGetAllEventsAsOrganizerFails() {
        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .get("/api/events/getAll")
                .then()
                .statusCode(403);
    }
}
