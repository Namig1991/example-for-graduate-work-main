package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ads;
import ru.skypro.homework.model.Images;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long> {
    List<Images> findByAds(Ads ads);
}
