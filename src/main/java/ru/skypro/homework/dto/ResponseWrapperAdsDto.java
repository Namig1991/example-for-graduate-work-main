package ru.skypro.homework.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResponseWrapperAdsDto {
  private Integer count = null;
  private List<AdsDto> results = null;

}
