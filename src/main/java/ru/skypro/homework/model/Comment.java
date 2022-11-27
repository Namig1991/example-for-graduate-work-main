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
    private LocalDateTime createdAt;
    private String text;

    @ManyToOne
    @JoinColumn(name = "ads_id")
    private Ads ads;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
