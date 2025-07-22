package fr.epita.assistants.item_producer.domain.entity;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.item_producer.data.model.ItemModel;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class GetResourceEntity {
    Integer id;
    Float quantity;
    ItemAggregate.ResourceType type;
}
