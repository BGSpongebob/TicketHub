package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class SellerResourceTest extends BaseResourceTest {

    @Test
    public void testGetAllSellersAsOrganizerSuccess() {
        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .get("/api/sellers/getAll")
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testGetAllSellersAsSellerFail() {
        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .get("/api/sellers/getAll")
                .then()
                .statusCode(403);
    }

    @Test
    public void testGetAllSellersWithSalesAsAdminSuccess() {
        given()
                .header("Authorization", "Bearer " + adminToken.token)
                .get("/api/sellers/getAllWithSales")
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testGetAllSellersWithSalesAsOrganizerFail() {
        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .get("/api/sellers/getAllWithSales")
                .then()
                .statusCode(403);
    }
}