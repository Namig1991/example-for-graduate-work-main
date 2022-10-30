package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import java.time.OffsetDateTime;

/**
 * AdsComment
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-29T11:17:35.092Z[GMT]")

@Data
public class AdsComment   {
  @JsonProperty("author")
  private Integer author = null;

  @JsonProperty("createdAt")
  private OffsetDateTime createdAt = null;

  @JsonProperty("pk")
  private Integer pk = null;

  @JsonProperty("text")
  private String text = null;
}
