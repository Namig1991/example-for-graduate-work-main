package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsDto;

public interface AdsService {
    ResponseWrapperAdsDto getAllAds();

    FullAdsDto getAds(Integer id);

    AdsDto createAds(CreateAdsDto createAdsDto, MultipartFile file, Authentication authentication);

    void removeAds(Integer id, String username, UserDetails userDetails);

    AdsDto updateAdvert(Integer id, AdsDto adsDto, String username, UserDetails userDetails, MultipartFile file);

    ResponseWrapperAdsDto findAds(String search);

    ResponseWrapperAdsDto getAdsMe(String name);

}
