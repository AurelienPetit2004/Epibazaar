package fr.epita.assistants.item_producer.data.repository;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.item_producer.data.model.ItemModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<ItemModel> {

    @Transactional
    public void clear(ItemModel itemModel)
    {
        this.delete("quantity = ?1 and type = ?2", itemModel.getQuantity(), itemModel.getType());
    }

    @Transactional
    public List<ItemModel> getAll()
    {
        return this.listAll();
    }

    @Transactional
    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        ItemModel itemModel = this.find("type", resourceType).firstResult();
        if (itemModel == null)
        {
            if (quantity <= 0f)
                return;
            itemModel = new ItemModel();
            itemModel.setType(resourceType);
            itemModel.setQuantity(quantity);
            this.persist(itemModel);
        }
        else
        {
            if (quantity <= 0f)
            {
                this.delete("type", itemModel.getType());
                return;
            }
            itemModel.setQuantity(quantity);
            this.persist(itemModel);
        }
    }

    @Transactional
    public ItemModel get(ItemAggregate.ResourceType resourceType)
    {
        return this.find("type", resourceType).firstResult();
    }

    @Transactional
    public void upgradeMoney(Float money)
    {
        ItemModel itemModel = this.find("type", ItemAggregate.ResourceType.MONEY).firstResult();
        if (itemModel.getQuantity() <= 0f)
        {
            this.delete("type", ItemAggregate.ResourceType.MONEY);
            return;
        }
        itemModel.setQuantity(money);
        this.persist(itemModel);
    }
}
