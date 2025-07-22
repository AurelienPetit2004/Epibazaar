package fr.epita.assistants.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@With
public class ItemInfo {
    private boolean isCollectable;
    private boolean isWalkable;

    private char value;

    private int collectQuantity;
    private Float price;
}
