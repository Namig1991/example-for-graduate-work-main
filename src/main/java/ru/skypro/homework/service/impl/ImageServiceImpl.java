package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repositories.ImageRepository;
import ru.skypro.homework.service.ImageService;
import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;


    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;


    }


    @Override
    public ResponseEntity<Void> updateImage(Long adsId, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<Image> getImageById(Long imageId) {
        return null;
    }
}
