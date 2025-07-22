package fr.epita.assistants.shop.domain.entity;

import jakarta.persistence.Column;
import lombok.Value;

@Value
public class ShopEntity {
    Integer id;
    Float priceMultiplier;
    Float upgradePrice;
}
