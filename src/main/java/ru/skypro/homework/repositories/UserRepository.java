package ru.skypro.homework.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Users;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    List<Users> findByEmailContains(String partOfEmail);
    List<Users> findByFirstNameContains(String partOfFirstName);
    List<Users> findByLastNameContains(String partOfLastName);
    List<Users> findByFirstNameContainsOrLastNameContains(String partOfFirstName,
                                                          String partOfLastName);
    List<Users> findByPhoneContains(String partOfPhone);
    List<Users> findByRoleIgnoreCaseOOrderByLastName (Role role);


}
