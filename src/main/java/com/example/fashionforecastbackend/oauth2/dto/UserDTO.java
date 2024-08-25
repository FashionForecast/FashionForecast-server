package com.example.fashionforecastbackend.oauth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String role;
    private String name;
    private String username;
    private String email;
    private String gender;
    private String age;

    @Override
    public String toString() {
        return "UserDTO{" +
                "role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}