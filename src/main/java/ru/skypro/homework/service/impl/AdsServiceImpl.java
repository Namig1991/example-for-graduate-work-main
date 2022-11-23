package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdsService;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
@Transactional
public class AdsServiceImpl implements AdsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsServiceImpl.class);
    private static final String END_POINT_FOR_IMAGE = "http://localhost:8080/ads/getImage/";
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;

    public AdsServiceImpl(AdsRepository adsRepository,
                          UserRepository userRepository, ImageService imageService, AdsMapper adsMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.adsMapper = adsMapper;
    }

    /**
     * Сохранение нового объявления
     */
    public ResponseEntity<AdsDto> saveAds(CreateAdsDto adsDto, MultipartFile file)
            throws IOException {
        LOGGER.info("Was invoked method for save Ads.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Ads newAds = adsMapper.createAdsDtoToAds(adsDto);
        Users author = userRepository.findByUsername(authentication.getName());
        newAds.setUsers(author);
        Ads intermediateSavedAds = adsRepository.save(newAds);
        Images savedImage = imageService.uploadImage(intermediateSavedAds.getId(), file);
        intermediateSavedAds.setImage(END_POINT_FOR_IMAGE + savedImage.getId());
        Ads savedAds = adsRepository.save(intermediateSavedAds);
        AdsDto returnedAdsDto = adsMapper.toAdsDto(savedAds);
        return ResponseEntity.ok(returnedAdsDto);
    }
}
