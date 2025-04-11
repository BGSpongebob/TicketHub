package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.DTOs.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class SaleResourceTest extends BaseResourceTest {

    @Test
    public void testCreateSaleAsSellerSuccess() {
        BasicEventDTO event = new BasicEventDTO();
        event.setId(sharedEvent.getId());
        event.setTitle(sharedEvent.getTitle());
        event.setDescription(sharedEvent.getDescription());
        event.setEtypeId(sharedEvent.getETypeId());
        event.setEventDateTime(sharedEvent.getEventDateTime());

        List<SaleTicketDTO> tickets = new ArrayList<>();
        SaleTicketDTO ticket = new SaleTicketDTO();
        ticket.setTicket(sharedEvent.getTickets().getFirst());
        ticket.setQuantity(5);
        tickets.add(ticket);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setSaleDate(LocalDate.now());
        saleDTO.setSeller(sellerToken.user.getSeller().getId());
        saleDTO.setClient(sharedClient);
        saleDTO.setEvent(event);
        saleDTO.setSalesTickets(tickets);


        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(saleDTO)
                .post("/api/sales/create")
                .then()
                .statusCode(201);
    }

    @Test
    public void testCreateSaleInvalidDataFails() {
        SaleDTO saleDTO = new SaleDTO();

        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(saleDTO)
                .post("/api/sales/create")
                .then()
                .statusCode(500);
    }

    @Test
    public void testCreateSaleAsOrganizerFails() {
        SaleDTO saleDTO = new SaleDTO();

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(saleDTO)
                .post("/api/sales/create")
                .then()
                .statusCode(403);
    }
}
