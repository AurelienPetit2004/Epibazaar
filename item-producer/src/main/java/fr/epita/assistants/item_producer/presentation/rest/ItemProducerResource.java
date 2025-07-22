package fr.epita.assistants.item_producer.presentation.rest;

import fr.epita.assistants.common.api.request.MoveRequest;
import fr.epita.assistants.common.api.request.StartRequest;
import fr.epita.assistants.common.api.response.*;
import fr.epita.assistants.common.command.*;
import fr.epita.assistants.item_producer.converter.DomainConverter;
import fr.epita.assistants.item_producer.domain.entity.*;
import fr.epita.assistants.item_producer.errors.ItemProducerError;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.ArrayList;
import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ItemProducerResource {

    @ConfigProperty(name="JWS_UPGRADE_COLLECT_COST") Float collect;
    @ConfigProperty(name="JWS_UPGRADE_MOVE_COST") Float move;
    @ConfigProperty(name = "JWS_UPGRADE_STAMINA_COST") Float stamina;

    @Inject
    @Channel("reset-inventory-command")
    Emitter<ResetInventoryCommand> emitter;

    @Inject
    @Channel("collect-item-command")
    Emitter<CollectItemCommand> collectItemCommandEmitter;

    @Inject
    @Channel("upgrade-collect-rate-command")
    Emitter<UpgradeCollectRateCommand> upgradeCollectRateCommandEmitter;

    @Inject
    @Channel("upgrade-movement-speed-command")
    Emitter<UpgradeMovementSpeedCommand> upgradeMovementSpeedCommandEmitter;

    @Inject
    @Channel("upgrade-stamina-command")
    Emitter<UpgradeStaminaCommand> upgradeStaminaCommandEmitter;

    @Inject
    DomainConverter domainConverter;

    @Path("/start")
    @POST
    public Response start(StartRequest request)
    {
        if (request.getMapPath() == null || request.getMapPath().isEmpty())
            return Response.status(400).entity(new ItemProducerError("no path")).build();
        GameEntity startEntity = domainConverter.start(request.getMapPath());
        if (startEntity == null)
            return Response.status(400).entity(new ItemProducerError("cannot start game")).build();
        emitter.send(new ResetInventoryCommand());
        return Response.status(200).entity(new StartResponse(startEntity.getMap())).build();
    }

    @Path("/player")
    @GET
    public Response getPlayer()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        GetPlayerEntity getPlayerEntity = domainConverter.getPlayer();
        if (getPlayerEntity == null)
            return Response.status(400).entity(new ItemProducerError("cannot get player")).build();
        return Response.status(200).entity(new PlayerResponse(getPlayerEntity.getPos_x(),
                getPlayerEntity.getPos_y(),
                getPlayerEntity.getLast_move(),
                getPlayerEntity.getLast_collect(),
                getPlayerEntity.getMove_speed_multiplier(),
                getPlayerEntity.getStamina_multiplier(),
                getPlayerEntity.getCollect_rate_multiplier())).build();
    }

    @Path("/")
    @GET
    public Response getResource()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        List<GetResourceEntity> getResourceEntity = domainConverter.getResource();
        if (getResourceEntity == null)
            return Response.status(400).entity(new ItemProducerError("cannot get resources")).build();
        ArrayList<ItemResponse> arrayList = new ArrayList<>();
        for (GetResourceEntity el : getResourceEntity)
        {
            arrayList.add(new ItemResponse(el.getId(), el.getQuantity(), el.getType()));
        }
        return Response.status(200).entity(new ItemsResponse(arrayList)).build();
    }

    @Path("/upgrades")
    @GET
    public Response getUpgrades()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        return Response.status(200).entity(new UpgradeCostResponse(collect, move, stamina)).build();
    }

    @Path("/move")
    @POST
    public Response move(MoveRequest moveRequest)
    {
        if (moveRequest == null || moveRequest.getDirection() == null)
            return Response.status(400).entity(new ItemProducerError("invalid direction in parameter")).build();
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        MoveEntity moveEntity = domainConverter.move(moveRequest.getDirection());
        if (moveEntity == null)
            return Response.status(400).entity(new ItemProducerError("invalid direction for player")).build();
        if (moveEntity.getPosY() == null && moveEntity.getPosX() == null)
            return Response.status(429).entity(new ItemProducerError("move too early")).build();
        return Response.status(200).entity(new MoveResponse(moveEntity.getPosX(), moveEntity.getPosY())).build();
    }

    @Path("/collect")
    @POST
    public Response collect()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        CollectEntity collectEntity = domainConverter.collect();
        if (collectEntity == null)
            return Response.status(400).entity(new ItemProducerError("cannot collect")).build();
        if (collectEntity.getMap() == null)
            return Response.status(429).entity(new ItemProducerError("collect too early")).build();
        collectItemCommandEmitter.send(new CollectItemCommand(collectEntity.getType(), collectEntity.getCollectRateMultiplier()));
        return Response.status(200).entity(new StartResponse(collectEntity.getMap())).build();
    }

    @Path("/upgrade/collect")
    @PATCH
    public Response upgradeCollect()
    {
        if (!domainConverter.isRunning())
        {
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        }
        Boolean res = domainConverter.upgrade(collect);
        if (res == null)
            return Response.status(404).entity(new ItemProducerError("money not found")).build();
        if (!res)
            return Response.status(400).entity(new ItemProducerError("not enough money")).build();
        upgradeCollectRateCommandEmitter.send(new UpgradeCollectRateCommand(collect));
        return Response.status(204).build();
    }

    @Path("/upgrade/move")
    @PATCH
    public Response upgradeMove()
    {
        if (!domainConverter.isRunning())
        {
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        }
        Boolean res = domainConverter.upgrade(move);
        if (res == null)
            return Response.status(404).entity(new ItemProducerError("money not found")).build();
        if (!res)
            return Response.status(400).entity(new ItemProducerError("not enough money")).build();
        upgradeMovementSpeedCommandEmitter.send(new UpgradeMovementSpeedCommand(move));
        return Response.status(204).build();
    }

    @Path("/upgrade/stamina")
    @PATCH
    public Response upgradeStamina()
    {
        if (!domainConverter.isRunning())
        {
            return Response.status(400).entity(new ItemProducerError("game not running")).build();
        }
        Boolean res = domainConverter.upgrade(stamina);
        if (res == null)
            return Response.status(404).entity(new ItemProducerError("money not found")).build();
        if (!res)
            return Response.status(400).entity(new ItemProducerError("not enough money")).build();
        upgradeStaminaCommandEmitter.send(new UpgradeStaminaCommand(stamina));
        return Response.status(204).build();
    }
}
