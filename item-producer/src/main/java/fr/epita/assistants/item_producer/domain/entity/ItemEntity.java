package fr.epita.assistants.item_producer.domain.entity;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import lombok.Value;

@Value
public class ItemEntity {
    Float quantity;
    ItemAggregate.ResourceType type;
}
