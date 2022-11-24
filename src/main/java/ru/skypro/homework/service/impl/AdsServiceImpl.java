package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAdsDto;
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdsService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AdsServiceImpl implements AdsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsServiceImpl.class);
    private static final String END_POINT_FOR_IMAGE = "http://localhost:8080/ads/getImage/";
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;

    public AdsServiceImpl(AdsRepository adsRepository,
                          UserRepository userRepository,
                          ImageService imageService,
                          AdsMapper adsMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.adsMapper = adsMapper;
    }

    /**
     * Сохранение нового объявления.
     *
     * @param adsDto - то, что получили от клиента.
     * @param file   - изображение к объявлению.
     */
    @Override
    public ResponseEntity<AdsDto> saveAds(CreateAdsDto adsDto, MultipartFile file)
            throws IOException {
        LOGGER.info("Was invoked method for save Ads.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Ads newAds = adsMapper.createAdsDtoToAds(adsDto);
        Users author = userRepository.findByUsername(authentication.getName());
        newAds.setUsers(author);
        adsRepository.save(newAds);
        Ads intermediateSavedAds = adsRepository.findByUsers(author).stream()
                .max(Comparator.comparing(Ads::getId))
                .orElseThrow();
        imageService.uploadImage(intermediateSavedAds.getId(), file);
        Images savedImage = imageService.getImagesByAds(intermediateSavedAds);
        intermediateSavedAds.setImage(END_POINT_FOR_IMAGE + savedImage.getId());
        adsRepository.save(intermediateSavedAds);
        AdsDto returnedAdsDto = adsMapper.toAdsDto(intermediateSavedAds);
        returnedAdsDto.setAuthor(Math.toIntExact(author.getId()));
        return ResponseEntity.ok(returnedAdsDto);
    }

    /**
     * Получение всех объявлений.
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        LOGGER.info("Was invoked method for get all Ads.");
        List<Ads> adsList = adsRepository.findAll();
        List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
        for (int i = 0; i < adsDtoList.size(); i++) {
            adsDtoList.get(i).setAuthor(Math.toIntExact(adsList.get(i).getUsers().getId()));
        }
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        responseWrapperAdsDto.setResults(adsDtoList);
        responseWrapperAdsDto.setCount(adsDtoList.size());
        return ResponseEntity.ok(responseWrapperAdsDto);
    }

    /**
     * Удаление объявления по его идентификатору.
     * @param adsId - идентификатор объявления.
     * @return - удаленное объявление.
     */
    @Override
    public ResponseEntity<AdsDto> removeAdsById(Long adsId) throws RuntimeException {
        LOGGER.info("Was invoked method for delete Ads by id.");
        Ads removedAds = adsRepository.findById(adsId).orElseThrow();
        imageService.removeImagesByAds(removedAds);
        adsRepository.deleteById(adsId);
        AdsDto removedAdsDto = adsMapper.toAdsDto(removedAds);
        removedAdsDto.setAuthor(Math.toIntExact(removedAds.getUsers().getId()));
        return ResponseEntity.ok(removedAdsDto);
    }

    @Override
    public ResponseEntity<FullAdsDto> getAdsById(Long adsId) {
        LOGGER.info("Was invoked method for get Ads by id.");
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        Users author = ads.getUsers();
        FullAdsDto fullAdsDto = adsMapper.toFullAdsDto(ads, author);
        return ResponseEntity.ok(fullAdsDto);
    }

    /**
     * Редактирование объявления.
     * @param adsId - идентификатор объявления.
     * @param adsDto - измененное объявление.
     * @return - измененное объявление.
     */
    @Override
    public ResponseEntity<AdsDto> updateAds(Long adsId, AdsDto adsDto) throws RuntimeException {
        LOGGER.info("Was invoked method for edit Ads.");
        Ads adsFromClient = adsMapper.toAds(adsDto);
        Ads adsFromDataBase = adsRepository.findById(adsId).orElseThrow();
        if (!(adsFromClient.getPrice().equals(adsFromDataBase.getPrice()))) {
            adsFromDataBase.setPrice(adsFromClient.getPrice());
        }
        if (adsFromClient.getTitle() != null &&
                !(adsFromClient.getTitle().equals(adsFromDataBase.getTitle()))) {
            adsFromDataBase.setTitle(adsFromClient.getTitle());
        }
        AdsDto returnedAdsDto = adsMapper.toAdsDto(adsFromDataBase);
        returnedAdsDto.setAuthor(Math.toIntExact(adsFromDataBase.getUsers().getId()));
        return ResponseEntity.ok(returnedAdsDto);
    }

    /**
     * Получение своих объявлений.
     * @return - список своих объявлений.
     */
    @Override
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication) {
        LOGGER.info("Was invoked method for get Ads by current user.");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users author = userRepository.findByUsername(authentication.getName());
        List<Ads> adsList = adsRepository.findByUsers(author);
        List<AdsDto> adsDtoList = adsMapper.listAdsToListAdsDto(adsList);
        for (AdsDto adsDto:adsDtoList) {
            adsDto.setAuthor(Math.toIntExact(author.getId()));
        }
        ResponseWrapperAdsDto responseWrapperAdsDto = new ResponseWrapperAdsDto();
        responseWrapperAdsDto.setResults(adsDtoList);
        responseWrapperAdsDto.setCount(adsDtoList.size());
        return ResponseEntity.ok(responseWrapperAdsDto);
    }
}
