package org.acme.DTOs;

public class TokenResponse {
    public String token;
    public UserDTO user;

    public TokenResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
