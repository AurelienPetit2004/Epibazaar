package fr.epita.assistants.common.api.response;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class PlayerResponse {
    Integer posX;
    Integer posY;
    LocalDateTime lastMove;
    LocalDateTime lastCollect;
    Float moveSpeedMultiplier;
    Float staminaMultiplier;
    Float collectRateMultiplier;
}
