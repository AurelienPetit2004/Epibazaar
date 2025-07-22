package fr.epita.assistants.item_producer.domain.service;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.item_producer.converter.DataConverter;
import fr.epita.assistants.item_producer.data.model.PlayerModel;
import fr.epita.assistants.item_producer.data.repository.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@ApplicationScoped
public class PlayerService {
    @ConfigProperty(name="JWS_TICK_DURATION") Long tick;
    @ConfigProperty(name="JWS_DELAY_MOVEMENT") Long delay;
    @ConfigProperty(name = "JWS_DELAY_COLLECT") Long delayCollect;

    @Inject
    DataConverter dataConverter;

    @Inject
    GameService gameService;

    public void start()
    {
        dataConverter.clearPlayer();
        dataConverter.addPlayer(1f, 1f, 0, 0, 1f, null, null);
    }

    public PlayerModel getPLayer()
    {
        if (gameService.getGame() == null)
            return null;
        return dataConverter.getPlayer();
    }

    public Boolean isRunning()
    {
        return dataConverter.hasPlayer();
    }

    public ArrayList<Integer> move(Integer x, Integer y) {
        PlayerModel playerModel = this.getPLayer();
        LocalDateTime lastMove = playerModel.getLastMove();
        if (lastMove != null)
        {
            long time = tick * delay;
            time /= playerModel.getMoveSpeedMultiplier().longValue();
            if (lastMove.plus(time, ChronoUnit.MILLIS).isAfter(LocalDateTime.now()))
            {
                ArrayList<Integer> res = new ArrayList<>();
                res.add(null);
                res.add(null);
                return res;
            }
        }
        ArrayList<ArrayList<String>> map = gameService.getMap();
        if (playerModel.getPosX() + x < 0 || playerModel.getPosX() + x >= map.getFirst().size())
            return null;
        if (playerModel.getPosY() + y < 0 || playerModel.getPosY() + y >= map.size())
            return null;
        String tile = map.get(playerModel.getPosY() + y).get(playerModel.getPosX() + x);
        char c = tile.charAt(0);
        if (tile.equals("WATER"))
            c = 'O';
        ItemAggregate.ResourceType resourceType = ItemAggregate.ResourceType.getResource(c);
        if (!resourceType.getItemInfo().isWalkable())
            return null;
        ArrayList<Integer> res = new ArrayList<>();
        res.add(playerModel.getPosX() + x);
        res.add(playerModel.getPosY() + y);
        dataConverter.updatePlayerPos(playerModel.getPosX() + x, playerModel.getPosY() + y);
        return res;
    }

    public Float getCollectMultiplier()
    {
        return this.getPLayer().getCollectRateMultiplier();
    }

    public Boolean onTimeCollect()
    {
        PlayerModel playerModel = this.getPLayer();
        LocalDateTime lastCollect = playerModel.getLastCollect();
        if (lastCollect != null)
        {
            long time = tick * delayCollect;
            time /= playerModel.getStaminaMultiplier().longValue();
            if (lastCollect.plus(time, ChronoUnit.MILLIS).isAfter(LocalDateTime.now()))
            {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public ItemAggregate.ResourceType collectable()
    {
        ArrayList<ArrayList<String>> map = gameService.getMap();
        PlayerModel playerModel = this.getPLayer();
        String tile = map.get(playerModel.getPosY()).get(playerModel.getPosX());
        char c = tile.charAt(0);
        if (tile.equals("WATER"))
            c = 'O';
        ItemAggregate.ResourceType itemAggregate = ItemAggregate.ResourceType.getResource(c);
        if (!itemAggregate.getItemInfo().isCollectable())
            return null;
        return itemAggregate;
    }

    public ArrayList<ArrayList<String>> collect()
    {
        PlayerModel playerModel = this.getPLayer();
        ArrayList<ArrayList<String>> map = gameService.getMap();
        map.get(playerModel.getPosY()).set(playerModel.getPosX(), "GROUND");
        StringBuilder stringBuilder = new StringBuilder();
        for (ArrayList<String> arr : map) {
            int num = 0;
            for (int i = 0; i < arr.size(); i++) {
                if (num < 9 && (i - 1 < 0 || arr.get(i).equals(arr.get(i - 1)))) {
                    num++;
                } else {
                    stringBuilder.append(num);
                    num = 1;
                    if (arr.get(i - 1).equals("ROCK"))
                        stringBuilder.append("R");
                    else if (arr.get(i - 1).equals("WOOD"))
                        stringBuilder.append("W");
                    else if (arr.get(i - 1).equals("GROUND")) {
                        stringBuilder.append("G");
                    } else if (arr.get(i - 1).equals("WATER"))
                        stringBuilder.append("O");
                    else if (arr.get(i - 1).equals("MONEY"))
                        stringBuilder.append("M");
                }
            }
            stringBuilder.append(num);
            if (arr.getLast().equals("ROCK"))
                stringBuilder.append("R");
            else if (arr.getLast().equals("WOOD"))
                stringBuilder.append("W");
            else if (arr.getLast().equals("GROUND")) {
                stringBuilder.append("G");
            } else if (arr.getLast().equals("WATER"))
                stringBuilder.append("O");
            else if (arr.getLast().equals("MONEY"))
                stringBuilder.append("M");
            stringBuilder.append(";");
        }
        String s = stringBuilder.substring(0, stringBuilder.length() - 1);
        dataConverter.updateMap(s);
        dataConverter.updateCollect();
        return map;
    }

    public void upgradeCollect(Float multiplier)
    {
        dataConverter.upgradeCollectPlayer(multiplier);
    }

    public void upgradeMove(Float multiplier)
    {
        dataConverter.upgradeMovePlayer(multiplier);
    }

    public void upgradeStamina(Float multiplier)
    {
        dataConverter.upgradeStaminaPlayer(multiplier);
    }
}
