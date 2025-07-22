package fr.epita.assistants.common.command;

import io.smallrye.common.constraint.NotNull;
import lombok.Value;

@Value
public class UpgradeShopPriceCommand {
    @NotNull
    Integer shopId;
    @NotNull
    Float price;
}
