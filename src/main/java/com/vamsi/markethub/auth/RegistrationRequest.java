package com.vamsi.markethub.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
    private String Role;

    public RegistrationRequest(){}

    public RegistrationRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        Role = role;
    }


}
