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
import ru.skypro.homework.dto.FullAdsDto;
import ru.skypro.homework.dto.ResponseWrapperAdsDto;
import ru.skypro.homework.mappers.AdsMapper;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.CommentRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AdsServiceImpl implements AdsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsServiceImpl.class);
    private static final String END_POINT_FOR_IMAGE = "/getImage/";
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final CommentRepository commentRepository;
    private final AdsMapper adsMapper;
    private final UserService userService;

    public AdsServiceImpl(AdsRepository adsRepository,
                          UserRepository userRepository,
                          ImageService imageService,
                          CommentRepository commentRepository,
                          AdsMapper adsMapper, UserService userService) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.commentRepository = commentRepository;
        this.adsMapper = adsMapper;
        this.userService = userService;
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
        Ads newAds = adsMapper.createAdsDtoToAds(adsDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users author = userRepository.findByUsername(authentication.getName());
        newAds.setUsers(author);
        Ads intermediateSavedAds = adsRepository.save(newAds);
        imageService.uploadImage(intermediateSavedAds.getId(), file);
        Images savedImage = imageService.getImagesByAds(intermediateSavedAds);
        intermediateSavedAds.setImage(END_POINT_FOR_IMAGE + savedImage.getId());
        Ads savedAds = adsRepository.save(intermediateSavedAds);
        AdsDto returnedAdsDto = adsMapper.toAdsDto(savedAds);
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
    public ResponseEntity<AdsDto> removeAdsById(Long adsId) {
        LOGGER.info("Was invoked method for delete Ads by id.");
        Ads removedAds = adsRepository.findById(adsId).orElseThrow();
        imageService.removeImagesByAds(removedAds);
        List<Comment> commentsOfAds = commentRepository.findByAds(removedAds);
        commentRepository.deleteAll(commentsOfAds);
        adsRepository.deleteById(adsId);
        AdsDto removedAdsDto = adsMapper.toAdsDto(removedAds);
        removedAdsDto.setAuthor(Math.toIntExact(removedAds.getUsers().getId()));
        return ResponseEntity.ok(removedAdsDto);
    }

    /**
     * Получение объявления по его идентификатору.
     * @param adsId - идентификатор объявления.
     * @return - найденное объявление.
     */
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
    public ResponseEntity<AdsDto> updateAds(Long adsId, AdsDto adsDto) {
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
        adsRepository.save(adsFromDataBase);
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

    /**
     * Обновление изображения к объявлению.
     * @param adsId - идентификатор объявления.
     * @param file - новое изображение.
     */
    @Override
    public void updateAdsImage(Long adsId, MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method for update image of Ads.");
        Ads updatedAds = adsRepository.findById(adsId).orElseThrow();
        imageService.removeImagesByAds(updatedAds);
        imageService.uploadImage(adsId, file);
        Images savedImage = imageService.getImagesByAds(updatedAds);
        updatedAds.setImage(END_POINT_FOR_IMAGE + savedImage.getId());
        updatedAds.setImages(savedImage);
        adsRepository.save(updatedAds);
    }
}
