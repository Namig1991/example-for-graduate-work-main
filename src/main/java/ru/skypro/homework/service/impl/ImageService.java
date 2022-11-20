package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;
import ru.skypro.homework.repositories.AdsRepository;
import ru.skypro.homework.repositories.ImageRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@Transactional
public class ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    /**
     * Директорий, где будут храниться файлы с фотографиями.
     */
    @Value("/marketPlace/images")
    private String imageDir;
    private ImageRepository imageRepository;
    private AdsRepository adsRepository;

    public void uploadImage(Long adsId, MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method for uploading image for Ads.");
        Images newImage = new Images();
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        newImage.setAds(ads);
        String pathOfAds = imageDir + "/" + adsId;
        if (file != null) {
            Path filePath = Path.of(imageDir, ads.getTitle() +
                    getImagesByAdsId(adsId).size() + "." +
                    getExtension(Objects.requireNonNull(file.getOriginalFilename())));

            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                    InputStream is = file.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
                    ){
                bis.transferTo(bos);
            }
            newImage.setFilePath(filePath.toString());
            newImage.setMediaType(file.getContentType());
            newImage.setFileSize(file.getSize());
        }
    }

    /**
     * Поиск изображений по идентификатору объявления.
     * @param adsId - идентификатор объявления.
     */
    private List<Images> getImagesByAdsId(Long adsId) {
        Ads ads = adsRepository.findById(adsId).orElseThrow();
        return imageRepository.findByAds(ads);
    }

    /**
     * Получение расширения файла с фотографией.
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
