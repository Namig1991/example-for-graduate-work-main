package ru.skypro.homework.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.model.Ads;

import java.io.IOException;

public interface AdsService {
    ResponseEntity<AdsDto> saveAds(CreateAdsDto createAdsDto, MultipartFile file) throws IOException;
}
