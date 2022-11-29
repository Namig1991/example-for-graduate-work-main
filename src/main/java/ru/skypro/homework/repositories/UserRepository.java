package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);
    Users findByRole(Role role);

}
