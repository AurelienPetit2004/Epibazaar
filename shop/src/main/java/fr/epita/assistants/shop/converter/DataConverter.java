package fr.epita.assistants.shop.converter;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.shop.data.model.ItemModel;
import fr.epita.assistants.shop.data.model.ShopModel;
import fr.epita.assistants.shop.data.repository.ItemRepository;
import fr.epita.assistants.shop.data.repository.ShopRepository;
import fr.epita.assistants.shop.domain.entity.ItemEntity;
import fr.epita.assistants.shop.domain.entity.ShopEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DataConverter {
    @Inject
    ItemRepository itemRepository;

    @Inject
    ShopRepository shopRepository;

    public void clearItem(ItemEntity itemEntity)
    {
        ItemModel itemModel = new ItemModel();
        itemModel.setType(itemEntity.getType());
        itemModel.setQuantity(itemEntity.getQuantity());
        itemRepository.clear(itemModel);
    }

    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        itemRepository.newItem(resourceType, quantity);
    }

    public void upgradeMoney(Float money)
    {
        itemRepository.upgradeMoney(money);
    }

    public void clearAllShops()
    {
        shopRepository.clearAllShops();
    }

    public void newShop(ShopEntity shopEntity)
    {
        shopRepository.newShop(shopEntity.getPriceMultiplier(), shopEntity.getUpgradePrice());
    }

    public Boolean isRunning()
    {
        return shopRepository.hasShop();
    }

    public ShopEntity getShop(Integer id)
    {
        ShopModel res = shopRepository.getShop(id);
        if (res == null)
            return null;
        return new ShopEntity(res.getId(), res.getPriceMultiplier(), res.getUpgradePrice());
    }

    public List<ShopEntity> getAllShops()
    {
        List<ShopModel> list = shopRepository.all();
        List<ShopEntity> res = new ArrayList<>();
        for (ShopModel shopModel : list)
        {
            res.add(new ShopEntity(shopModel.getId(), shopModel.getPriceMultiplier(), shopModel.getUpgradePrice()));
        }
        return res;
    }

    public List<ItemModel> getAllItems()
    {
        return itemRepository.getAll();
    }

    public ItemModel getMoney()
    {
        return itemRepository.getMoney();
    }

    public ItemModel getItem(ItemAggregate.ResourceType resourceType)
    {
        return itemRepository.getItem(resourceType);
    }

    public Float getUpgradePrice(Integer id)
    {
        return shopRepository.getShop(id).getUpgradePrice();
    }

    public void upgradePriceMultiplier(Integer id, Float upgradePriceMultiplier)
    {
        shopRepository.upgradePriceMultiplier(id, upgradePriceMultiplier);
    }
}
