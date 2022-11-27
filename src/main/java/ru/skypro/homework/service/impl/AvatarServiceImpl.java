package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Avatar;
import ru.skypro.homework.model.Users;
import ru.skypro.homework.repositories.AvatarRepository;
import ru.skypro.homework.repositories.UserRepository;
import ru.skypro.homework.service.AvatarService;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarServiceImpl.class);

    /**
     * Директорий, где будут храниться файлы с аватарками.
     */
    @Value("/marketPlace/avatars")
    private String avatarDir;
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    public AvatarServiceImpl(AvatarRepository avatarRepository, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    /**
     * Сохранение аватара пользователя.
     * @param userId - идентификатор пользователя.
     * @param file - изображение аватара.
     * @return - сохраненный аватар.
     */
    public Avatar uploadAvatar(Long userId, MultipartFile file) throws IOException {
        LOGGER.info("Was invoked method for uploading avatar for user.");
        Avatar avatar = new Avatar();
        Users user = userRepository.findById(userId).orElseThrow();
        avatar.setUsers(user);
        String pathOfAvatar = avatarDir + "/" + userId;
        if (file != null) {
            Path filePath = Path.of(pathOfAvatar, user.getLastName()  + "." +
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
            avatar.setFilePath(filePath.toString());
            avatar.setMediaType(file.getContentType());
            avatar.setFileSize(file.getSize());
            return avatarRepository.save(avatar);
        }
        return null;
    }

    /**
     * Получение расширения файла с фотографией.
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
