package fr.epita.assistants.shop.presentation.rest;

import fr.epita.assistants.common.aggregate.ItemAggregate;
import fr.epita.assistants.common.api.request.ItemsRequest;
import fr.epita.assistants.common.api.response.*;
import fr.epita.assistants.common.command.CreateShopCommand;
import fr.epita.assistants.common.command.SellItemCommand;
import fr.epita.assistants.common.command.SyncInventoryCommand;
import fr.epita.assistants.common.command.UpgradeShopPriceCommand;
import fr.epita.assistants.shop.converter.DomainConverter;
import fr.epita.assistants.shop.domain.entity.ItemEntity;
import fr.epita.assistants.shop.domain.entity.ShopEntity;
import fr.epita.assistants.shop.errors.ShopError;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShopResource {

    @Inject
    @Channel("sync-inventory-command")
    Emitter<SyncInventoryCommand> emitter;

    @Inject
    @Channel("create-shop-command")
    Emitter<CreateShopCommand> createShopCommandEmitter;

    @Inject
    @Channel("sell-item-command")
    Emitter<SellItemCommand> sellItemCommandEmitter;

    @Inject
    @Channel("upgrade-shop-price-command")
    Emitter<UpgradeShopPriceCommand> upgradeShopPriceCommandEmitter;

    @Inject
    DomainConverter domainConverter;

    @Path("/start")
    @POST
    public Response start()
    {
        domainConverter.startShop();
        emitter.send(new SyncInventoryCommand());
        return Response.status(204).build();
    }

    @Path("/{id}")
    @GET
    public Response getShop(@PathParam("id") final Integer id)
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game is not running")).build();
        if (id <= 0)
            return Response.status(400).entity(new ShopError("invalid id")).build();
        ShopEntity res = domainConverter.getShop(id);
        if (res == null)
            return Response.status(404).entity(new ShopError("shop not found")).build();
        return Response.status(200).entity(new ShopResponse(res.getId(), res.getPriceMultiplier(), res.getUpgradePrice())).build();
    }

    @Path("/")
    @GET
    public Response getShops()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game not running")).build();
        List<ShopEntity> shops = domainConverter.getAllShops();
        ArrayList<ShopResponse> res = new ArrayList<>();
        for (ShopEntity shopEntity : shops)
        {
            res.add(new ShopResponse(shopEntity.getId(), shopEntity.getPriceMultiplier(), shopEntity.getUpgradePrice()));
        }
        return Response.status(200).entity(new ShopsResponse(res)).build();
    }

    @Path("/resources")
    @GET
    public Response getResources()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game not running")).build();
        List<ItemEntity> list = domainConverter.getAllItems();
        if (list == null)
            return Response.status(400).entity(new ShopError("cannot get resources")).build();
        ArrayList<ItemResponse> res = new ArrayList<>();
        for (ItemEntity item : list)
        {
            res.add(new ItemResponse(item.getId(), item.getQuantity(), item.getType()));
        }
        return Response.status(200).entity(new ItemsResponse(res)).build();
    }

    @Path("/price")
    @GET
    public Response getPrice()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game not running")).build();
        Float shopPrice = domainConverter.getPrice();
        return Response.status(200).entity(new ShopPriceResponse(shopPrice)).build();
    }

    @Path("/")
    @POST
    public Response createShop()
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game not running")).build();
        Integer success = domainConverter.createShop();
        if (success == null)
            return Response.status(404).entity(new ShopError("no money found")).build();
        if (success < 0)
            return Response.status(400).entity(new ShopError("not enough money")).build();
        if (success == 0)
            return Response.status(400).entity(new ShopError("max number of shops reached")).build();
        Float shopPrice = domainConverter.getPrice();
        createShopCommandEmitter.send(new CreateShopCommand(shopPrice));
        return Response.status(204).build();
    }

    @Path("/sell/{id}")
    @PATCH
    public Response sellShop(@PathParam("id") final Integer id, ItemsRequest itemsRequest)
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game not running")).build();
        HashMap<ItemAggregate.ResourceType, Float> hashMap = domainConverter.isSellableValid(itemsRequest.getItemsRequest());
        if (hashMap == null)
            return Response.status(400).entity(new ShopError("invalid amount")).build();
        if (hashMap.containsKey(null))
            return Response.status(400).entity(new ShopError("item not sellable")).build();
        Boolean check = domainConverter.checkResource(hashMap);
        if (check ==  null)
            return Response.status(404).entity(new ShopError("item not found")).build();
        if (!check)
            return Response.status(400).entity(new ShopError("not enough resources")).build();
        Float price = domainConverter.getId(id);
        if (price == null)
            return Response.status(404).entity(new ShopError("shop not found")).build();
        List<ItemAggregate> res = domainConverter.hashToAggregate(hashMap);
        sellItemCommandEmitter.send(new SellItemCommand(res, price));
        return Response.status(204).build();
    }

    @Path("/upgrade/price/{id}")
    @PATCH
    public Response upgradePrice(@PathParam("id") final Integer id)
    {
        if (!domainConverter.isRunning())
            return Response.status(400).entity(new ShopError("game not running")).build();
        if (!domainConverter.hasMoney())
            return Response.status(404).entity(new ShopError("no money found")).build();
        Boolean check = domainConverter.checkPrice(id);
        if (check == null)
            return Response.status(404).entity(new ShopError("shop not found")).build();
        if (!check)
            return Response.status(400).entity(new ShopError("not enough money")).build();
        upgradeShopPriceCommandEmitter.send(new UpgradeShopPriceCommand(id, domainConverter.getUpgradePrice(id)));
        return Response.status(204).build();
    }
}
