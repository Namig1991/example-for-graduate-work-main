package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "author_id")
    private Integer author;

    @Column(name = "comment_create")
    private LocalDateTime createdAt;

    @Column(name = "comment_text")
    private String text;

    @Column(name = "ads_id")
    private Long adsId;
}
