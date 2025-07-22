package fr.epita.assistants.item_producer.domain.service;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.item_producer.converter.DataConverter;
import fr.epita.assistants.item_producer.data.model.ItemModel;
import fr.epita.assistants.item_producer.domain.entity.ItemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

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

    public List<ItemModel> getResource()
    {
        return dataConverter.getResource();
    }

    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        dataConverter.newItem(resourceType, quantity);
    }

    public Boolean upgrade(Float value)
    {
        ItemModel money = dataConverter.getMoney();
        if (money == null)
            return null;
        if (money.getQuantity() - value < 0)
        {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public void upgradeMoney(Float money)
    {
        dataConverter.upgradeMoney(money);
    }

    public void updateItems(List<ItemEntity> list)
    {
        for (ItemEntity itemEntity : list)
        {
            dataConverter.newItem(itemEntity.getType(), itemEntity.getQuantity());
        }
    }
}
