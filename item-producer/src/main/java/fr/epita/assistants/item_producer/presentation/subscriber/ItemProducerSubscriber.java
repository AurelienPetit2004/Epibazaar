package fr.epita.assistants.item_producer.presentation.subscriber;

import fr.epita.assistants.common.aggregate.*;
import fr.epita.assistants.item_producer.converter.DomainConverter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class ItemProducerSubscriber {

    @ConfigProperty(name = "JWS_UPGRADE_MULTIPLIER") Float upgradeMultiplier;

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
        domainConverter.upgradeMoneyCollect(upgradeItemProducerAggregate.getNewMoney(), upgradeMultiplier);
    }

    @Incoming("upgrade-movement-speed-aggregate")
    public void upgradeMove(UpgradeItemProducerAggregate upgradeItemProducerAggregate)
    {
        domainConverter.upgradeMoneyMove(upgradeItemProducerAggregate.getNewMoney(), upgradeMultiplier);
    }

    @Incoming("upgrade-stamina-aggregate")
    public void upgradeStamina(UpgradeItemProducerAggregate upgradeItemProducerAggregate)
    {
        domainConverter.upgradeMoneyStamina(upgradeItemProducerAggregate.getNewMoney(), upgradeMultiplier);
    }

    @Incoming("create-shop-aggregate")
    public void updateMoneyCreate(ItemAggregate itemAggregate)
    {
        domainConverter.updateMoney(itemAggregate.getQuantity());
    }

    @Incoming("sell-item-aggregate")
    public void receiveSell(SellItemAggregate sellItemAggregate)
    {
        domainConverter.updateItems(sellItemAggregate.getItems());
    }

    @Incoming("upgrade-shop-price-aggregate")
    public void receiveShopUpgrade(UpgradeShopAggregate upgradeShopAggregate)
    {
        domainConverter.updateMoney(upgradeShopAggregate.getNewMoney());
    }
}
