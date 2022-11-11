package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer author;
    private LocalDateTime createdAt;
    private String text;
    private Long adsId;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
