package com.vamsi.markethub.auth;

import lombok.Getter;
import lombok.Setter;

public class LoginResponse {

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private  String role;

    public LoginResponse(){}

    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
}
