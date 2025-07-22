package fr.epita.assistants.item_producer.data.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "game")
@Data
public class GameModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(columnDefinition = "TEXT") private String map;
}
