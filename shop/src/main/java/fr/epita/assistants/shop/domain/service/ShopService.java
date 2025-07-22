package fr.epita.assistants.shop.domain.service;

import fr.epita.assistants.shop.converter.DataConverter;
import fr.epita.assistants.shop.data.model.ItemModel;
import fr.epita.assistants.shop.data.model.ShopModel;
import fr.epita.assistants.shop.domain.entity.ShopEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ShopService {
    @Inject
    DataConverter dataConverter;

    @Inject
    ItemService itemService;

    public void startShop(Float upgradeCost)
    {
        dataConverter.clearAllShops();
        dataConverter.newShop(new ShopEntity(null, 1f, upgradeCost));
    }

    public Boolean isRunning()
    {
        return dataConverter.isRunning();
    }

    public ShopEntity getShop(Integer id)
    {
        return dataConverter.getShop(id);
    }

    public List<ShopEntity> getAllShops()
    {
        return dataConverter.getAllShops();
    }

    public Integer createShop(Float shopPrice, Integer maxQuantity)
    {
        if (dataConverter.getAllShops().size() >= maxQuantity)
            return 0;
        Boolean res = itemService.enoughMoney(shopPrice);
        if (res == null)
            return null;
        if (!res)
            return -1;
        return 1;
    }

    public void newShop(Float upgradeCost)
    {
        dataConverter.newShop(new ShopEntity(null, 1f, upgradeCost));
    }

    public Float getId(Integer id)
    {
        ShopEntity shopEntity = dataConverter.getShop(id);
        if (shopEntity == null)
            return null;
        return shopEntity.getPriceMultiplier();
    }

    public Boolean checkPrice(Integer id)
    {
        ItemModel money = dataConverter.getMoney();
        ShopEntity shopEntity = dataConverter.getShop(id);
        if (shopEntity == null)
            return null;
        if (money.getQuantity() < shopEntity.getUpgradePrice())
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    public Float getUpgradePrice(Integer id)
    {
        return dataConverter.getUpgradePrice(id);
    }

    public void upgradePriceMultiplier(Integer id, Float upgradePriceMultiplier)
    {
        dataConverter.upgradePriceMultiplier(id, upgradePriceMultiplier);
    }
}
