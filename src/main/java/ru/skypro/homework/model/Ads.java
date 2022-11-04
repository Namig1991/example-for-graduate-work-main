package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "ads")
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    private Long id;

    @Column(name = "ads_author_id")
    private Integer author;

    @Column(name = "ads_image")
    private String image;

    @Column(name = "ads_price")
    private Integer price;

    @Column(name = "ads_title")
    private String title;

    @Column(name = "ads_description")
    private String description;
}
