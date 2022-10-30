package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * FullAds
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-29T11:17:35.092Z[GMT]")

@Data
public class FullAds   {
  @JsonProperty("authorFirstName")
  private String authorFirstName = null;

  @JsonProperty("authorLastName")
  private String authorLastName = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("image")
  private String image = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("pk")
  private Integer pk = null;

  @JsonProperty("price")
  private Integer price = null;

  @JsonProperty("title")
  private String title = null;
}
