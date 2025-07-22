package fr.epita.assistants.item_producer.converter;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.common.aggregate.ResetInventoryAggregate;
import fr.epita.assistants.common.utils.Direction;
import fr.epita.assistants.item_producer.data.model.ItemModel;
import fr.epita.assistants.item_producer.data.model.PlayerModel;
import fr.epita.assistants.item_producer.domain.entity.*;
import fr.epita.assistants.item_producer.domain.service.GameService;
import fr.epita.assistants.item_producer.domain.service.ItemService;
import fr.epita.assistants.item_producer.domain.service.PlayerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DomainConverter {
    @Inject
    GameService gameService;

    @Inject
    PlayerService playerService;

    @Inject
    ItemService itemService;

    public GameEntity start(String mapPath)
    {
        ArrayList<ArrayList<String>> array = gameService.start(mapPath);
        if (array == null)
            return null;
        playerService.start();
        return new GameEntity(array);
    }

    public GetPlayerEntity getPlayer()
    {
        PlayerModel playerModel = playerService.getPLayer();
        if (playerModel == null)
            return null;
        return new GetPlayerEntity(playerModel.getCollectRateMultiplier(),
                playerModel.getMoveSpeedMultiplier(),
                playerModel.getPosX(),
                playerModel.getPosY(),
                playerModel.getStaminaMultiplier(),
                playerModel.getLastCollect(),
                playerModel.getLastMove());
    }

    public void clearItem(ResetInventoryAggregate resetInventoryAggregate)
    {
        List<ItemEntity> list = new ArrayList<>();
        for (ItemAggregate el : resetInventoryAggregate.getItems())
        {
            list.add(new ItemEntity(el.getQuantity(), el.getType()));
        }
        itemService.clear(list);
    }

    public List<GetResourceEntity> getResource()
    {
        List<ItemModel> itemModels = itemService.getResource();
        List<GetResourceEntity> list = new ArrayList<>();
        for (ItemModel el : itemModels)
        {
            list.add(new GetResourceEntity(el.getId(), el.getQuantity(), el.getType()));
        }
        return list;
    }

    public Boolean isRunning()
    {
        return gameService.isRunning() && playerService.isRunning();
    }

    public MoveEntity move(Direction direction)
    {
        ArrayList<Integer> res = playerService.move(direction.getPoint().getPosX(), direction.getPoint().getPosY());
        if (res == null)
            return null;
        return new MoveEntity(res.getFirst(), res.getLast());
    }

    public CollectEntity collect()
    {
        if (!playerService.onTimeCollect())
        {
            return new CollectEntity(null, null, null);
        }
        ItemAggregate.ResourceType resourceType = playerService.collectable();
        if (resourceType == null)
            return null;
        Float multiplier = playerService.getCollectMultiplier();
        return new CollectEntity(playerService.collect(), resourceType, multiplier);
    }

    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        itemService.newItem(resourceType, quantity);
    }

    public Boolean upgrade(Float value)
    {
        return itemService.upgrade(value);
    }

    public void upgradeMoneyCollect(Float money, Float multiplier)
    {
        itemService.upgradeMoney(money);
        playerService.upgradeCollect(multiplier);
    }

    public void upgradeMoneyMove(Float money, Float multiplier)
    {
        itemService.upgradeMoney(money);
        playerService.upgradeMove(multiplier);
    }

    public void upgradeMoneyStamina(Float money, Float multiplier)
    {
        itemService.upgradeMoney(money);
        playerService.upgradeStamina(multiplier);
    }

    public void updateMoney(Float money)
    {
        itemService.upgradeMoney(money);
    }

    public void updateItems(List<ItemAggregate> list)
    {
        List<ItemEntity> itemEntities = new ArrayList<>();
        for (ItemAggregate itemAggregate : list)
            itemEntities.add(new ItemEntity(itemAggregate.getQuantity(), itemAggregate.getType()));
        itemService.updateItems(itemEntities);
    }
}
