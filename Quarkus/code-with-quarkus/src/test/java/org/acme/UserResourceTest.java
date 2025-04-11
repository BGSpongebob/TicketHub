package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.DTOs.OrganizerDTO;
import org.acme.DTOs.SellerDTO;
import org.acme.DTOs.TokenResponse;
import org.acme.DTOs.UserDTO;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class UserResourceTest extends BaseResourceTest {

    @Test
    public void testRegisterOrganizerSuccess() {
        OrganizerDTO organizerDTO = new OrganizerDTO();
        organizerDTO.setName1("Test Organizer");
        organizerDTO.setName2("Test Organizer");
        organizerDTO.setEmail(generateUniqueEmail());
        organizerDTO.setPhone(generateUniquePhone());
        TokenResponse response = loginUser(registerUser(organizerDTO));
        assert response != null;
    }

    @Test
    public void testRegisterSellerSuccess() {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName1("Test Seller");
        sellerDTO.setName2("Test Seller");
        sellerDTO.setEmail(generateUniqueEmail());
        sellerDTO.setPhone(generateUniquePhone());
        TokenResponse response = loginUser(registerUser(sellerDTO));
        assert response.token != null;
    }

    @Test
    public void testRegisterAdminSuccess() {
        TokenResponse response = loginUser(registerUser(null));
        assert response.token != null;
    }

    @Test
    public void testRegisterDuplicateUsernameFails() {
        String username = generateUniqueUsername();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword("password123");
        userDTO.setRoleId(3L);

        given().contentType(ContentType.JSON).body(userDTO).post("/api/users/register").then().statusCode(201);
        given().contentType(ContentType.JSON).body(userDTO).post("/api/users/register").then().statusCode(409);
    }

    @Test
    public void testLoginSuccess() {
        TokenResponse response = loginUser(registerUser(null));
        assert response.token != null;
    }

    @Test
    public void testLoginInvalidCredentialsFails() {
        String username = registerUser(null);
        UserDTO loginDTO = new UserDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword("wrongpass");
        given().contentType(ContentType.JSON).body(loginDTO).post("/api/users/login").then().statusCode(401);
    }
}
