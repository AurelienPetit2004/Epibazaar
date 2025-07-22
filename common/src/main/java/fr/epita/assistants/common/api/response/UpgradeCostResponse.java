package fr.epita.assistants.common.api.response;

import lombok.Value;

@Value
public class UpgradeCostResponse {
    Float upgradeCollectCost;
    Float upgradeMoveCost;
    Float upgradeStaminaCost;
}
