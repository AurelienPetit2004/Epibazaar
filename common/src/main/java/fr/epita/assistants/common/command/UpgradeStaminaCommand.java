package fr.epita.assistants.common.command;

import io.smallrye.common.constraint.NotNull;
import lombok.Value;

@Value
public class UpgradeStaminaCommand {
    @NotNull
    Float price;
}
