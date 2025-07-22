package fr.epita.assistants.common.command;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import io.smallrye.common.constraint.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class SellItemCommand {
    @NotNull
    List<ItemAggregate> items;
    @NotNull
    Float priceMultiplier;
}
