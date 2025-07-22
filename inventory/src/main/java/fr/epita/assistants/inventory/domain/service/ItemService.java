package fr.epita.assistants.inventory.domain.service;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.inventory.converter.DataConverter;
import fr.epita.assistants.inventory.data.model.ItemModel;
import fr.epita.assistants.inventory.domain.entity.ItemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ItemService {
    @Inject
    DataConverter dataConverter;

    public List<ItemEntity> itemStart()
    {
        List<ItemModel> itemModels = dataConverter.getItems();
        List<ItemEntity> res = new ArrayList<>();
        for (ItemModel el : itemModels)
        {
            res.add(new ItemEntity(el.getType(), el.getQuantity()));
        }
        dataConverter.clear();
        return res;
    }

    public ItemModel newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        return dataConverter.newItem(resourceType, quantity);
    }

    public Float upgrade(Float value)
    {
        return dataConverter.upgrade(value);
    }

    public List<ItemEntity> getAllItems()
    {
        List<ItemModel> itemModels = dataConverter.getItems();
        List<ItemEntity> res = new ArrayList<>();
        for (ItemModel el : itemModels)
        {
            res.add(new ItemEntity(el.getType(), el.getQuantity()));
        }
        return res;
    }

    public List<ItemModel> updateItems(List<ItemEntity> itemEntities, Float priceMultiplier)
    {
        Float money = 0f;
        List<ItemModel> res = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntities)
        {
            res.add(dataConverter.updateItem(itemEntity.getType(), itemEntity.getQuantity()));
            money += (itemEntity.getType().getItemInfo().getPrice() * itemEntity.getQuantity() * priceMultiplier);
        }
        res.add(dataConverter.updateMoney(money));
        return res;
    }
}
