package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByUsers(Users author);
    List<Comment> findByCreatedAt(LocalDateTime dateTime);
    List<Comment> findByCreatedAtBefore(LocalDateTime dateTime);
    List<Comment> findByCreatedAtAfter(LocalDateTime dateTime);
    List<Comment> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Comment> findByTextContains(String partOfText);
    List<Comment> findByAdsId(Long adsId);

}
