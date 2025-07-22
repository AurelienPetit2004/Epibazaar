package fr.epita.assistants.inventory.domain.entity;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import io.smallrye.common.constraint.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class ItemEntity {
    ItemAggregate.ResourceType type;
    Float quantity;
}
