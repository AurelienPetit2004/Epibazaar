package fr.epita.assistants.inventory.data.repository;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.inventory.data.model.ItemModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<ItemModel> {

    @Transactional
    public List<ItemModel> getItems()
    {
        return this.listAll();
    }

    @Transactional
    public void clear()
    {
        this.deleteAll();
    }

    @Transactional
    public ItemModel newItem(ItemAggregate.ResourceType resourceType, Float quantity) {
        ItemModel itemModel = this.find("type", resourceType).firstResult();
        if (itemModel == null)
        {
            itemModel = new ItemModel();
            itemModel.setType(resourceType);
            itemModel.setQuantity(quantity);
            if (itemModel.getQuantity() > 0f)
                this.persist(itemModel);
        }
        else
        {
            itemModel.setQuantity(itemModel.getQuantity() + quantity);
            if (itemModel.getQuantity() <= 0f)
            {
                this.delete("type", itemModel.getType());
            }
            else
                this.persist(itemModel);
        }
        return itemModel;
    }

    @Transactional
    public Float upgradeMoney(Float value)
    {
        ItemModel itemModel = this.find("type", ItemAggregate.ResourceType.MONEY).firstResult();
        itemModel.setQuantity(itemModel.getQuantity() - value);
        if (itemModel.getQuantity() <= 0f)
        {
            this.delete("type", ItemAggregate.ResourceType.MONEY);
        }
        else
            this.persist(itemModel);
        return itemModel.getQuantity();
    }

    @Transactional
    public ItemModel updateItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        ItemModel itemModel = this.find("type", resourceType).firstResult();
        itemModel.setQuantity(itemModel.getQuantity() - quantity);
        if (itemModel.getQuantity() <= 0f)
        {
            this.delete("type", itemModel.getType());
        }
        else
            this.persist(itemModel);
        return itemModel;
    }

    @Transactional
    public ItemModel updateMoney(Float money)
    {
        ItemModel itemModel = this.find("type", ItemAggregate.ResourceType.MONEY).firstResult();
        if (itemModel == null)
        {
            itemModel = new ItemModel();
            itemModel.setType(ItemAggregate.ResourceType.MONEY);
            itemModel.setQuantity(money);
            if (itemModel.getQuantity() > 0f)
                this.persist(itemModel);
        }
        else
        {
            itemModel.setQuantity(itemModel.getQuantity() + money);
            if (itemModel.getQuantity() <= 0f)
            {
                this.delete("type", ItemAggregate.ResourceType.MONEY);
            }
            else
                this.persist(itemModel);
        }
        return itemModel;
    }
}
