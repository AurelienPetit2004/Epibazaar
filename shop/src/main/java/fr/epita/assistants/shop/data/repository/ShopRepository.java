package fr.epita.assistants.shop.data.repository;

import fr.epita.assistants.shop.data.model.ShopModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ShopRepository implements PanacheRepository<ShopModel> {
    @Transactional
    public void clearAllShops()
    {
        this.deleteAll();
    }

    @Transactional
    public void newShop(Float priceMultiplier, Float upgradeCost)
    {
        ShopModel shopModel = new ShopModel();
        shopModel.setUpgradePrice(upgradeCost);
        shopModel.setPriceMultiplier(priceMultiplier);
        this.persist(shopModel);
    }

    @Transactional
    public Boolean hasShop()
    {
        if (this.listAll().isEmpty())
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    @Transactional
    public ShopModel getShop(Integer id)
    {
        return this.find("id", id).firstResult();
    }

    @Transactional
    public List<ShopModel> all()
    {
        return this.listAll();
    }

    @Transactional
    public void upgradePriceMultiplier(Integer id, Float upgradePriceMultiplier)
    {
        ShopModel shopModel = this.find("id", id).firstResult();
        shopModel.setPriceMultiplier(shopModel.getPriceMultiplier() * upgradePriceMultiplier);
        this.persist(shopModel);
    }
}
