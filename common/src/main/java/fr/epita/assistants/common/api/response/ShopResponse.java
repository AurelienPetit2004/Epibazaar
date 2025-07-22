package fr.epita.assistants.common.api.response;

import lombok.Value;

@Value
public class ShopResponse {
    Integer id;
    Float priceMultiplier;
    Float upgradePrice;
}
