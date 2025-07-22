package fr.epita.assistants.common.api.response;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import lombok.Value;

@Value
public class ItemResponse {
    Integer id;
    Float quantity;
    ItemAggregate.ResourceType type;
}
