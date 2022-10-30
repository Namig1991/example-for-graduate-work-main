package ru.skypro.homework.dto;

import lombok.Data;
@Data
public class NewPasswordDto {
  private String currentPassword = null;
  private String newPassword = null;

}
