package fr.epita.assistants.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Direction {
    UP(new Point(0, -1)),
    DOWN(new Point(0, 1)),
    RIGHT(new Point(1, 0)),
    LEFT(new Point(-1, 0));

    private final Point point;
}
