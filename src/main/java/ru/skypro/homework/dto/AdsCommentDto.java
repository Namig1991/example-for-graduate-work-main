package ru.skypro.homework.dto;

import java.time.OffsetDateTime;
import lombok.Data;


@Data
public class AdsCommentDto {
  private Integer author;
  private OffsetDateTime createdAt;
  private Integer pk;
  private String text;
}
