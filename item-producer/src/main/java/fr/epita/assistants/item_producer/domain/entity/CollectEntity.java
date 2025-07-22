package fr.epita.assistants.item_producer.domain.entity;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import lombok.Value;

import java.util.ArrayList;

@Value
public class CollectEntity {
    ArrayList<ArrayList<String>> map;
    ItemAggregate.ResourceType type;
    Float collectRateMultiplier;
}
