package ru.skypro.homework.model;

import lombok.*;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_first_name")
    private String firstName;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_phone")
    private String phone;

    @Column(name = "user_role")
    private Role role;

    @Column(name = "user_password")
    private String password;
}
