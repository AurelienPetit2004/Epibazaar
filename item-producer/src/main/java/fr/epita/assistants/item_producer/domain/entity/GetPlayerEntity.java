package fr.epita.assistants.item_producer.domain.entity;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class GetPlayerEntity {
    Float collect_rate_multiplier;
    Float move_speed_multiplier;
    Integer pos_x;
    Integer pos_y;
    Float stamina_multiplier;
    LocalDateTime last_collect;
    LocalDateTime last_move;
}
