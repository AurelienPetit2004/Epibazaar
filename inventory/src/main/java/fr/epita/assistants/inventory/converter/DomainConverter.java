package fr.epita.assistants.inventory.converter;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.inventory.data.model.ItemModel;
import fr.epita.assistants.inventory.domain.entity.ItemEntity;
import fr.epita.assistants.inventory.domain.service.ItemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DomainConverter {
    @Inject
    ItemService itemService;

    public List<ItemAggregate> itemStart()
    {
        List<ItemEntity> itemEntities = itemService.itemStart();
        List<ItemAggregate> res = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntities)
        {
            res.add(new ItemAggregate(itemEntity.getType(), itemEntity.getQuantity()));
        }
        return res;
    }

    public ItemAggregate newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        ItemModel itemModel = itemService.newItem(resourceType, quantity);
        return new ItemAggregate(itemModel.getType(), itemModel.getQuantity());
    }

    public ItemEntity upgrade(Float value)
    {
        Float newMoney = itemService.upgrade(value);
        return new ItemEntity(ItemAggregate.ResourceType.MONEY, newMoney);
    }

    public List<ItemAggregate> getAllItems()
    {
        List<ItemEntity> itemEntities = itemService.getAllItems();
        List<ItemAggregate> res = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntities)
        {
            res.add(new ItemAggregate(itemEntity.getType(), itemEntity.getQuantity()));
        }
        return res;
    }

    public List<ItemAggregate> updateItems(List<ItemAggregate> itemAggregates, Float priceMultiplier) {
        List<ItemEntity> list = new ArrayList<>();
        for (ItemAggregate itemAggregate : itemAggregates)
            list.add(new ItemEntity(itemAggregate.getType(), itemAggregate.getQuantity()));
        List<ItemModel> itemModels = itemService.updateItems(list, priceMultiplier);
        List<ItemAggregate> itemAggregateList = new ArrayList<>();
        for (ItemModel itemModel : itemModels)
        {
            itemAggregateList.add(new ItemAggregate(itemModel.getType(), itemModel.getQuantity()));
        }
        return itemAggregateList;
    }
}
