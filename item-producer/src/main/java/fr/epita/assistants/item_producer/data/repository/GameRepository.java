package fr.epita.assistants.item_producer.data.repository;

import fr.epita.assistants.item_producer.data.model.GameModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GameRepository implements PanacheRepository<GameModel> {

    @Transactional
    public void clear()
    {
        this.deleteAll();
    }

    @Transactional
    public void add(GameModel gameModel)
    {
        this.persist(gameModel);
    }

    @Transactional
    public GameModel getGame()
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
    public void updateMap(String s)
    {
        GameModel gameModel = this.getGame();
        gameModel.setMap(s);
        this.persist(gameModel);
    }
}
