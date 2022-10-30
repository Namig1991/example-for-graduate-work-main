package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * NewPassword
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-10-29T11:17:35.092Z[GMT]")

@Data
public class NewPassword   {
  @JsonProperty("currentPassword")
  private String currentPassword = null;

  @JsonProperty("newPassword")
  private String newPassword = null;
}
