package fr.epita.assistants.common.api.request;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import lombok.Value;

@Value
public class ItemRequest {
    Float quantity;
    ItemAggregate.ResourceType type;
}
