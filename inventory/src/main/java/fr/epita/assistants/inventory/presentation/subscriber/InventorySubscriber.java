package fr.epita.assistants.inventory.presentation.subscriber;

import fr.epita.assistants.common.aggregate.*;
import fr.epita.assistants.common.command.*;
import fr.epita.assistants.inventory.converter.DomainConverter;
import fr.epita.assistants.inventory.domain.entity.ItemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import java.util.List;

@ApplicationScoped
public class InventorySubscriber {
    @Inject
    DomainConverter domainConverter;

    @Incoming("reset-inventory-command")
    @Outgoing("reset-inventory-aggregate")
    public ResetInventoryAggregate receiveStart(ResetInventoryCommand resetInventoryCommand)
    {
        List<ItemAggregate> res = domainConverter.itemStart();
        return new ResetInventoryAggregate(res);
    }

    @Incoming("collect-item-command")
    @Outgoing("collect-item-aggregate")
    public ItemAggregate receiveCollect(CollectItemCommand collectItemCommand)
    {
        Integer quantity = collectItemCommand.getType().getItemInfo().getCollectQuantity();
        return domainConverter.newItem(collectItemCommand.getType(), collectItemCommand.getCollectRateMultiplier() * quantity);
    }

    @Incoming("upgrade-collect-rate-command")
    @Outgoing("upgrade-collect-rate-aggregate")
    public UpgradeItemProducerAggregate upgradeCollect(UpgradeCollectRateCommand upgradeCollectRateCommand)
    {
        ItemEntity itemEntity = domainConverter.upgrade(upgradeCollectRateCommand.getPrice());
        return new UpgradeItemProducerAggregate(itemEntity.getQuantity());
    }

    @Incoming("upgrade-movement-speed-command")
    @Outgoing("upgrade-movement-speed-aggregate")
    public UpgradeItemProducerAggregate upgradeMove(UpgradeMovementSpeedCommand upgradeMovementSpeedCommand)
    {
        ItemEntity itemEntity = domainConverter.upgrade(upgradeMovementSpeedCommand.getPrice());
        return new UpgradeItemProducerAggregate(itemEntity.getQuantity());
    }

    @Incoming("upgrade-stamina-command")
    @Outgoing("upgrade-stamina-aggregate")
    public UpgradeItemProducerAggregate upgradeStamina(UpgradeStaminaCommand upgradeStaminaCommand)
    {
        ItemEntity itemEntity = domainConverter.upgrade(upgradeStaminaCommand.getPrice());
        return new UpgradeItemProducerAggregate(itemEntity.getQuantity());
    }

    @Incoming("sync-inventory-command")
    @Outgoing("sync-inventory-aggregate")
    public SyncInventoryAggregate receiveStartShop(SyncInventoryCommand syncInventoryCommand)
    {
        List<ItemAggregate> res = domainConverter.getAllItems();
        return new SyncInventoryAggregate(res);
    }

    @Incoming("create-shop-command")
    @Outgoing("create-shop-aggregate")
    public ItemAggregate receiveCreateShop(CreateShopCommand createShopCommand)
    {
        ItemEntity itemEntity = domainConverter.upgrade(createShopCommand.getPrice());
        return new ItemAggregate(itemEntity.getType(), itemEntity.getQuantity());
    }

    @Incoming("sell-item-command")
    @Outgoing("sell-item-aggregate")
    public SellItemAggregate receiveSell(SellItemCommand sellItemCommand)
    {
        List<ItemAggregate> res = domainConverter.updateItems(sellItemCommand.getItems(), sellItemCommand.getPriceMultiplier());
        return new SellItemAggregate(res);
    }

    @Incoming("upgrade-shop-price-command")
    @Outgoing("upgrade-shop-price-aggregate")
    public UpgradeShopAggregate receiveShopUpgrade(UpgradeShopPriceCommand upgradeShopPriceCommand)
    {
        ItemEntity newMoney = domainConverter.upgrade(upgradeShopPriceCommand.getPrice());
        return new UpgradeShopAggregate(upgradeShopPriceCommand.getShopId(), newMoney.getQuantity());
    }
}
