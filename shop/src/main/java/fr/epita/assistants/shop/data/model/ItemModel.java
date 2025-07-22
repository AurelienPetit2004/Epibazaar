package fr.epita.assistants.shop.data.model;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Data
public class ItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    private Float quantity;
    @Enumerated(EnumType.STRING) private ItemAggregate.ResourceType type;
}
