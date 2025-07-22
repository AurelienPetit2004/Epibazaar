package fr.epita.assistants.common.aggregate;

import io.smallrye.common.constraint.NotNull;
import lombok.Value;

@Value
public class UpgradeShopAggregate {
    @NotNull
    Integer shopId;
    @NotNull
    Float newMoney;
}
