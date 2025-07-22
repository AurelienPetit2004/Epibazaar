package fr.epita.assistants.shop.domain.service;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.shop.converter.DataConverter;
import fr.epita.assistants.shop.data.model.ItemModel;
import fr.epita.assistants.shop.domain.entity.ItemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ItemService {
    @Inject
    DataConverter dataConverter;

    public void clear(List<ItemEntity> itemEntityList)
    {
        for (ItemEntity el : itemEntityList)
        {
            dataConverter.clearItem(el);
        }
    }

    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        dataConverter.newItem(resourceType, quantity);
    }

    public void upgradeMoney(Float money)
    {
        dataConverter.upgradeMoney(money);
    }

    public List<ItemModel> getAllItems()
    {
        return dataConverter.getAllItems();
    }

    Boolean enoughMoney(Float money)
    {
        ItemModel itemModel = dataConverter.getMoney();
        if (itemModel == null)
            return null;
        if (itemModel.getQuantity() < money)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public Boolean isSellableValid(HashMap<ItemAggregate.ResourceType, Float> hashMap)
    {
        for (Map.Entry<ItemAggregate.ResourceType, Float> el : hashMap.entrySet())
        {
            ItemAggregate.ResourceType key = el.getKey();
            Float val = el.getValue();
            if (key.getItemInfo().getPrice() == null)
                return Boolean.FALSE;
            if (val <= 0)
                return null;
        }
        return Boolean.TRUE;
    }

    public Boolean checkResource(HashMap<ItemAggregate.ResourceType, Float> hashMap)
    {
        for (Map.Entry<ItemAggregate.ResourceType, Float> el : hashMap.entrySet())
        {
            ItemAggregate.ResourceType key = el.getKey();
            Float val = el.getValue();
            ItemModel res = dataConverter.getItem(key);
            if (res == null)
                return null;
            if (res.getQuantity() < val)
                return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void updateItems(List<ItemEntity> list)
    {
        for (ItemEntity itemEntity : list)
        {
            dataConverter.newItem(itemEntity.getType(), itemEntity.getQuantity());
        }
    }

    public Boolean hasMoney()
    {
        ItemModel itemModel = dataConverter.getMoney();
        if (itemModel == null)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }
}
