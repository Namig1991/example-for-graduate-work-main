package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
