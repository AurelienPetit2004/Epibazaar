package fr.epita.assistants.item_producer.data.repository;

import fr.epita.assistants.item_producer.data.model.GameModel;
import fr.epita.assistants.item_producer.data.model.PlayerModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.time.LocalDateTime;

@ApplicationScoped
public class PlayerRepository implements PanacheRepository<PlayerModel> {

    @Transactional
    public void clear()
    {
        this.deleteAll();
    }

    @Transactional
    public void add(PlayerModel playerModel)
    {
        this.persist(playerModel);
    }

    @Transactional
    public PlayerModel getPlayer()
    {
        return this.listAll().getFirst();
    }

    @Transactional
    public Boolean exist()
    {
        if (this.listAll().isEmpty())
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    @Transactional
    public void update(Integer x, Integer y)
    {
        PlayerModel playerModel = this.listAll().getFirst();
        playerModel.setPosX(x);
        playerModel.setPosY(y);
        playerModel.setLastMove(LocalDateTime.now());
        this.persist(playerModel);
    }

    @Transactional
    public void updateCollect()
    {
        PlayerModel playerModel = this.getPlayer();
        playerModel.setLastCollect(LocalDateTime.now());
        this.persist(playerModel);
    }

    @Transactional
    public void upgradeCollect(Float multiplier)
    {
        PlayerModel playerModel = this.getPlayer();
        playerModel.setCollectRateMultiplier(playerModel.getCollectRateMultiplier() * multiplier);
        this.persist(playerModel);
    }

    @Transactional
    public void upgradeMove(Float multiplier)
    {
        PlayerModel playerModel = this.getPlayer();
        playerModel.setMoveSpeedMultiplier(playerModel.getMoveSpeedMultiplier() * multiplier);
        this.persist(playerModel);
    }

    @Transactional
    public void upgradeStamina(Float multiplier)
    {
        PlayerModel playerModel = this.getPlayer();
        playerModel.setStaminaMultiplier(playerModel.getStaminaMultiplier() * multiplier);
        this.persist(playerModel);
    }
}
