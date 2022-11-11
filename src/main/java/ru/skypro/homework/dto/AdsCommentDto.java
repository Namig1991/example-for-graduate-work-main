package ru.skypro.homework.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Data;


@Data
public class AdsCommentDto {
  private Integer author;
  private LocalDateTime createdAt;
  private Integer pk;
  private String text;
}
