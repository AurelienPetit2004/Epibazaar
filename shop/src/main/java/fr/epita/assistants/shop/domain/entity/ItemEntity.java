package fr.epita.assistants.shop.domain.entity;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import lombok.Value;

@Value
public class ItemEntity {
    Integer id;
    Float quantity;
    ItemAggregate.ResourceType type;
}
