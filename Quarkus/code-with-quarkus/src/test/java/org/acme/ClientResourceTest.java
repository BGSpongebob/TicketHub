package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.DTOs.ClientDTO;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class ClientResourceTest extends BaseResourceTest {

    @Test
    public void testCreateClientAsSellerSuccess() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName1("Client");
        clientDTO.setName2("Client");
        clientDTO.setPhone(generateUniquePhone());

        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(clientDTO)
                .post("/api/clients/create")
                .then()
                .statusCode(201)
                .body("name1", equalTo("Client"));
    }

    @Test
    public void testCreateClientAsOrganizerFails() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName1("Client");
        clientDTO.setName2("Client");
        clientDTO.setPhone(generateUniquePhone());

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(clientDTO)
                .post("/api/clients/create")
                .then()
                .statusCode(403);
    }

    @Test
    public void testCreateClientInvalidDataFails() {
        ClientDTO clientDTO = new ClientDTO();

        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(clientDTO)
                .post("/api/clients/create")
                .then()
                .statusCode(409);
    }

    @Test
    public void testGetAllClientsAsSellerSuccess() {
        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .get("/api/clients/getAll")
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testGetAllClientsAsOrganizerFails() {
        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .get("/api/clients/getAll")
                .then()
                .statusCode(403);
    }
}
