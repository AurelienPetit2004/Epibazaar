package fr.epita.assistants.shop.converter;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.common.aggregate.ResetInventoryAggregate;
import fr.epita.assistants.common.api.request.ItemRequest;
import fr.epita.assistants.shop.data.model.ItemModel;
import fr.epita.assistants.shop.domain.entity.ItemEntity;
import fr.epita.assistants.shop.domain.entity.ShopEntity;
import fr.epita.assistants.shop.domain.service.ItemService;
import fr.epita.assistants.shop.domain.service.ShopService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DomainConverter {

    @ConfigProperty(name="JWS_UPGRADE_PRICE_COST") Float upgradeCost;
    @ConfigProperty(name="JWS_MAX_SHOP_QUANTITY") Integer maxQuantity;
    @ConfigProperty(name="JWS_SHOP_PRICE") Float shopPrice;
    @ConfigProperty(name = "JWS_UPGRADE_MULTIPLIER") Float upgradeMultiplier;

    @Inject
    ItemService itemService;

    @Inject
    ShopService shopService;

    public void clearItem(ResetInventoryAggregate resetInventoryAggregate)
    {
        List<ItemEntity> list = new ArrayList<>();
        for (ItemAggregate el : resetInventoryAggregate.getItems())
        {
            list.add(new ItemEntity(null, el.getQuantity(), el.getType()));
        }
        itemService.clear(list);
    }

    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        itemService.newItem(resourceType, quantity);
    }

    public void upgradeMoney(Float money)
    {
        itemService.upgradeMoney(money);
    }

    public void startShop()
    {
        Config config = ConfigProvider.getConfig();
        shopPrice = config.getValue("JWS_SHOP_PRICE", Float.class);
        shopService.startShop(upgradeCost);
    }

    public Boolean isRunning()
    {
        return shopService.isRunning();
    }

    public ShopEntity getShop(Integer id)
    {
        return shopService.getShop(id);
    }

    public List<ShopEntity> getAllShops()
    {
        return shopService.getAllShops();
    }

    public List<ItemEntity> getAllItems()
    {
        List<ItemModel> itemModels = itemService.getAllItems();
        List<ItemEntity> list = new ArrayList<>();
        for (ItemModel el : itemModels)
        {
            list.add(new ItemEntity(el.getId(), el.getQuantity(), el.getType()));
        }
        return list;
    }

    public Integer createShop()
    {
        return shopService.createShop(shopPrice, maxQuantity);
    }

    public Float getPrice()
    {
        return shopPrice;
    }

    public void updateShopPrice()
    {
        shopPrice *= upgradeMultiplier;
    }

    public void newShop()
    {
        shopService.newShop(upgradeCost);
    }

    public HashMap<ItemAggregate.ResourceType, Float> isSellableValid(ArrayList<ItemRequest> items)
    {
        HashMap<ItemAggregate.ResourceType, Float> hashMap = new HashMap<>();
        for (ItemRequest el : items)
        {
            if (!hashMap.containsKey(el.getType()))
                hashMap.put(el.getType(), el.getQuantity());
            else
                hashMap.put(el.getType(), hashMap.get(el.getType()) + el.getQuantity());
        }
        Boolean res = itemService.isSellableValid(hashMap);
        if (res == null)
            return null;
        if (!res)
        {
            hashMap.clear();
            hashMap.put(null, null);
        }
        return hashMap;
    }

    public Boolean checkResource(HashMap<ItemAggregate.ResourceType, Float> hashMap)
    {
        return itemService.checkResource(hashMap);
    }

    public Float getId(Integer id)
    {
        return shopService.getId(id);
    }

    public List<ItemAggregate> hashToAggregate(HashMap<ItemAggregate.ResourceType, Float> hashMap)
    {
        List<ItemAggregate> res = new ArrayList<>();
        for (Map.Entry<ItemAggregate.ResourceType, Float> el : hashMap.entrySet())
        {
            ItemAggregate.ResourceType key = el.getKey();
            Float value = el.getValue();
            res.add(new ItemAggregate(key, value));
        }
        return res;
    }

    public void updateItems(List<ItemAggregate> list)
    {
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (ItemAggregate itemAggregate : list)
            itemEntities.add(new ItemEntity(null, itemAggregate.getQuantity(), itemAggregate.getType()));
        itemService.updateItems(itemEntities);
    }

    public Boolean hasMoney()
    {
        return itemService.hasMoney();
    }

    public Boolean checkPrice(Integer id)
    {
        return shopService.checkPrice(id);
    }

    public Float getUpgradePrice(Integer id)
    {
        return shopService.getUpgradePrice(id);
    }

    public void upgradeShop(Integer id, Float money)
    {
        itemService.upgradeMoney(money);
        shopService.upgradePriceMultiplier(id, upgradeMultiplier);
    }
}
