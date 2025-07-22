package fr.epita.assistants.shop.presentation.subscriber;

import fr.epita.assistants.common.aggregate.*;
import fr.epita.assistants.shop.converter.DomainConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ShopSubscriber {
    @Inject
    DomainConverter domainConverter;

    @Incoming("reset-inventory-aggregate")
    public void receiveInventory(ResetInventoryAggregate resetInventoryAggregate)
    {
        domainConverter.clearItem(resetInventoryAggregate);
    }

    @Incoming("collect-item-aggregate")
    public void receiveItem(ItemAggregate itemAggregate)
    {
        domainConverter.newItem(itemAggregate.getType(), itemAggregate.getQuantity());
    }

    @Incoming("upgrade-collect-rate-aggregate")
    public void upgradeCollect(UpgradeItemProducerAggregate upgradeItemProducerAggregate)
    {
        domainConverter.upgradeMoney(upgradeItemProducerAggregate.getNewMoney());
    }

    @Incoming("upgrade-movement-speed-aggregate")
    public void upgradeMove(UpgradeItemProducerAggregate upgradeItemProducerAggregate)
    {
        domainConverter.upgradeMoney(upgradeItemProducerAggregate.getNewMoney());
    }

    @Incoming("upgrade-stamina-aggregate")
    public void upgradeStamina(UpgradeItemProducerAggregate upgradeItemProducerAggregate)
    {
        domainConverter.upgradeMoney(upgradeItemProducerAggregate.getNewMoney());
    }

    @Incoming("sync-inventory-aggregate")
    public void receiveAllItems(SyncInventoryAggregate syncInventoryAggregate)
    {
        for (ItemAggregate itemAggregate : syncInventoryAggregate.getItems())
        {
            domainConverter.newItem(itemAggregate.getType(), itemAggregate.getQuantity());
        }
    }

    @Incoming("create-shop-aggregate")
    public void receiveCreateShop(ItemAggregate itemAggregate)
    {
        domainConverter.upgradeMoney(itemAggregate.getQuantity());
        domainConverter.updateShopPrice();
        domainConverter.newShop();
    }

    @Incoming("sell-item-aggregate")
    public void receiveSell(SellItemAggregate sellItemAggregate)
    {
        domainConverter.updateItems(sellItemAggregate.getItems());
    }

    @Incoming("upgrade-shop-price-aggregate")
    public void receiveShopUpgrade(UpgradeShopAggregate upgradeShopAggregate)
    {
        domainConverter.upgradeShop(upgradeShopAggregate.getShopId(), upgradeShopAggregate.getNewMoney());
    }
}
