package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.DTOs.RatingDTO;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class RatingResourceTest extends BaseResourceTest {

    @Test
    public void testCreateRatingAsOrganizerSuccess() {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRating(5);
        ratingDTO.setOrganizerId(organizerToken.user.getOrganizer().getId());
        ratingDTO.setSellerId(sellerToken.user.getSeller().getId());

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(ratingDTO)
                .post("/api/ratings/create")
                .then()
                .statusCode(201)
                .body("rating", equalTo(5));
    }

    @Test
    public void testCreateRatingAsSellerFails() {
        RatingDTO ratingDTO = new RatingDTO();

        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .contentType(ContentType.JSON)
                .body(ratingDTO)
                .post("/api/ratings/create")
                .then()
                .statusCode(403);
    }

    @Test
    public void testUpdateRatingAsOrganizerSuccess() {
        sharedRating.setRating(4);

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(sharedRating)
                .put("/api/ratings/update")
                .then()
                .statusCode(200)
                .body("rating", equalTo(4));
    }

    @Test
    public void testGetRatingsByOrganizerSuccess() {
        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .get("/api/ratings/getByOrganizer/" + organizerToken.user.getOrganizer().getId())
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }
}
