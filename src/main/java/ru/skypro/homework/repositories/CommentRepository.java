package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
