package fr.epita.assistants.shop.data.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shop")
@Data
public class ShopModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(name = "price_multiplier") private Float priceMultiplier;
    @Column(name = "upgrade_price") private Float upgradePrice;
}
