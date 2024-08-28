package com.example.fashionforecastbackend.oauth2.dto;

public record UserDTO(String role, String name, String username, String email, String gender, String age) {

    public static UserDTO of(String role, String name, String username, String email, String gender, String age) {
        return new UserDTO(role, name, username, email, gender, age);
    }

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