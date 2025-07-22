package fr.epita.assistants.common.api.request;

import fr.epita.assistants.common.utils.Direction;
import lombok.Value;

@Value
public class MoveRequest {
    Direction direction;
}
