package fr.epita.assistants.inventory.converter;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.inventory.data.model.ItemModel;
import fr.epita.assistants.inventory.data.repository.ItemRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DataConverter {
    @Inject
    ItemRepository itemRepository;

    public List<ItemModel> getItems()
    {
        return itemRepository.getItems();
    }

    public void clear()
    {
        itemRepository.clear();
    }

    public ItemModel newItem(ItemAggregate.ResourceType resourceType, Float quantity) {
        return itemRepository.newItem(resourceType, quantity);
    }

    public Float upgrade(Float value)
    {
        return itemRepository.upgradeMoney(value);
    }

    public ItemModel updateItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        return itemRepository.updateItem(resourceType, quantity);
    }

    public ItemModel updateMoney(Float money)
    {
        return itemRepository.updateMoney(money);
    }
}
