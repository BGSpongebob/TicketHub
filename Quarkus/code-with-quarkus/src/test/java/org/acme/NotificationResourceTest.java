package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.DTOs.NotificationDTO;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class NotificationResourceTest extends BaseResourceTest {

    @Test
    public void testCreateNotificationAsOrganizerSuccess() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Test Notification");
        notificationDTO.setText("Hello");
        notificationDTO.setUserId(sellerToken.user.getId());

        given()
                .header("Authorization", "Bearer " + organizerToken.token)
                .contentType(ContentType.JSON)
                .body(notificationDTO)
                .post("/api/notifications/create")
                .then()
                .statusCode(201);
    }

    @Test
    public void testReadNotificationSuccess() {
        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .put("/api/notifications/read/" + 1)
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetNotificationsByUserIdSuccess() {
        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .get("/api/notifications/getByUserId/" + organizerToken.user.getId())
                .then()
                .statusCode(200)
                .body("findAll.size()", greaterThan(0));
    }

    @Test
    public void testDeleteNotificationSuccess() {
        given()
                .header("Authorization", "Bearer " + sellerToken.token)
                .delete("/api/notifications/delete/" + 2)
                .then()
                .statusCode(204);
    }
}
