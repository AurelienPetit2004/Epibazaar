package fr.epita.assistants.common.command;

import io.smallrye.common.constraint.NotNull;
import lombok.Value;

import static fr.epita.assistants.common.aggregate.ItemAggregate.ResourceType;

@Value
public class CollectItemCommand {
    @NotNull
    ResourceType type;
    @NotNull
    Float collectRateMultiplier;
}