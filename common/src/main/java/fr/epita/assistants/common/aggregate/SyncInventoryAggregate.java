package fr.epita.assistants.common.aggregate;

import io.smallrye.common.constraint.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class SyncInventoryAggregate {
    @NotNull
    List<ItemAggregate> items;
}