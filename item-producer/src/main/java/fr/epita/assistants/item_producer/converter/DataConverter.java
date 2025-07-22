package fr.epita.assistants.item_producer.converter;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.item_producer.data.model.GameModel;
import fr.epita.assistants.item_producer.data.model.ItemModel;
import fr.epita.assistants.item_producer.data.model.PlayerModel;
import fr.epita.assistants.item_producer.data.repository.GameRepository;
import fr.epita.assistants.item_producer.data.repository.ItemRepository;
import fr.epita.assistants.item_producer.data.repository.PlayerRepository;
import fr.epita.assistants.item_producer.domain.entity.ItemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class DataConverter {
    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    ItemRepository itemRepository;

    public void clearGame()
    {
        gameRepository.clear();
    }

    public void clearPlayer()
    {
        playerRepository.clear();
    }

    public void clearItem(ItemEntity itemEntity)
    {
        ItemModel itemModel = new ItemModel();
        itemModel.setType(itemEntity.getType());
        itemModel.setQuantity(itemEntity.getQuantity());
        itemRepository.clear(itemModel);
    }

    public void addGame(String s)
    {
        GameModel gameModel = new GameModel();
        gameModel.setMap(s);
        gameRepository.add(gameModel);
    }

    public void addPlayer(Float collect_rate_multiplier, Float move_speed_multiplier, Integer pos_x, Integer pos_y, Float stamina_multiplier, LocalDateTime last_collect, LocalDateTime last_move)
    {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setCollectRateMultiplier(collect_rate_multiplier);
        playerModel.setMoveSpeedMultiplier(move_speed_multiplier);
        playerModel.setStaminaMultiplier(stamina_multiplier);
        playerModel.setPosX(pos_x);
        playerModel.setPosY(pos_y);
        playerModel.setLastCollect(last_collect);
        playerModel.setLastMove(last_move);
        playerRepository.add(playerModel);
    }

    public PlayerModel getPlayer()
    {
        return playerRepository.getPlayer();
    }

    public GameModel getGame()
    {
        return gameRepository.getGame();
    }

    public List<ItemModel> getResource()
    {
        return itemRepository.getAll();
    }

    public Boolean isRunning()
    {
        return gameRepository.exist();
    }

    public Boolean hasPlayer()
    {
        return playerRepository.exist();
    }

    public void updatePlayerPos(Integer x, Integer y)
    {
        playerRepository.update(x, y);
    }

    public void updateMap(String s)
    {
        gameRepository.updateMap(s);
    }

    public void updateCollect()
    {
        playerRepository.updateCollect();
    }

    public void newItem(ItemAggregate.ResourceType resourceType, Float quantity)
    {
        itemRepository.newItem(resourceType, quantity);
    }

    public ItemModel getMoney()
    {
        return itemRepository.get(ItemAggregate.ResourceType.MONEY);
    }

    public void upgradeMoney(Float money)
    {
        itemRepository.upgradeMoney(money);
    }

    public void upgradeCollectPlayer(Float multiplier)
    {
        playerRepository.upgradeCollect(multiplier);
    }

    public void upgradeMovePlayer(Float multiplier)
    {
        playerRepository.upgradeMove(multiplier);
    }

    public void upgradeStaminaPlayer(Float multiplier)
    {
        playerRepository.upgradeStamina(multiplier);
    }
}
