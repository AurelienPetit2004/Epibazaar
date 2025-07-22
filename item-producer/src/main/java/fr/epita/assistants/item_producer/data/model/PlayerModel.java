package fr.epita.assistants.item_producer.data.model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "player")
@Data
public class PlayerModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name = "collect_rate_multiplier") private Float collectRateMultiplier;
    @Column(name = "move_speed_multiplier") private Float moveSpeedMultiplier;
    @Column(name = "pos_x") private Integer posX;
    @Column(name = "pos_y") private Integer posY;
    @Column(name = "stamina_multiplier") private Float staminaMultiplier;
    @Column(name = "last_collect") private LocalDateTime lastCollect;
    @Column(name = "last_move") private LocalDateTime lastMove;
}
