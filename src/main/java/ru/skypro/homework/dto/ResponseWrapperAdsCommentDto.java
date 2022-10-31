package ru.skypro.homework.dto;

import java.util.List;
import lombok.Data;

@Data
public class ResponseWrapperAdsCommentDto {
  private Integer count = null;
  private List<AdsCommentDto> results = null;

}
