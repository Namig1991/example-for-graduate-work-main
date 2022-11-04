package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class LoginReqDto {
    private String password;
    private String username;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
