package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ads")
public class Ads {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private Integer author;
    private String image;
    private Integer price;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
}
