package fr.epita.assistants.common.aggregate;

import io.smallrye.common.constraint.NotNull;
import lombok.Value;

@Value
public class UpgradeItemProducerAggregate {
    @NotNull
    Float newMoney;
}
