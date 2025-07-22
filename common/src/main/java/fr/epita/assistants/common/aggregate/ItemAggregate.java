package fr.epita.assistants.common.aggregate;

import fr.epita.assistants.common.utils.ItemInfo;
import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
public class ItemAggregate {
    @NotNull
    public ResourceType type;
    @NotNull
    public Float quantity;
    @Getter
    @AllArgsConstructor
    public enum ResourceType {
        MONEY(new ItemInfo()
                .withCollectable(false)
                .withWalkable(false)
                .withCollectQuantity(0)
                .withPrice(null)
                .withValue('M')
        ),
        GROUND(new ItemInfo()
                .withCollectable(false)
                .withWalkable(true)
                .withCollectQuantity(0)
                .withPrice(null)
                .withValue('G')
        ),
        WATER(new ItemInfo()
                .withCollectable(false)
                .withWalkable(false)
                .withCollectQuantity(0)
                .withPrice(null)
                .withValue('O')
        ),
        ROCK(new ItemInfo()
                .withCollectable(true)
                .withWalkable(true)
                .withCollectQuantity(3)
                .withPrice(3f)
                .withValue('R')
        ),
        WOOD(new ItemInfo()
                .withCollectable(true)
                .withWalkable(true)
                .withCollectQuantity(5)
                .withPrice(2f)
                .withValue('W')
        );

        private static final Map<Character, ResourceType> lookup = new HashMap<>();

        static {
            for (ResourceType entity : ResourceType.values()) {
                lookup.put(entity.getItemInfo().getValue(), entity);
            }
        }

        private final ItemInfo itemInfo;

        public static ResourceType getResource(Character c) {
            return lookup.get(c);
        }
    }
}
