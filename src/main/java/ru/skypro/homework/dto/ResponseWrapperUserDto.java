package ru.skypro.homework.dto;

import java.util.List;
import lombok.Data;

@Data
public class ResponseWrapperUserDto {
  private Integer count;
  private List<UserDto> results;
}
