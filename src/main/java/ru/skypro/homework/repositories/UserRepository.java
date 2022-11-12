package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Users;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

}
