package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Result;
import services.PlayerService;

import java.util.List;

@Controller
public class Players extends play.mvc.Controller {

    @Autowired
    private PlayerService playerService;

    public Promise<Result> getAll() {
        Promise<List<Player>> playersPromise = playerService.getAllPlayers();
        return playersPromise.map(new F.Function<List<Player>, Result>() {
            @Override
            public Result apply(List<Player> players) throws Throwable {
                if (players != null) {
                    return ok(Json.toJson(players));
                }
                return notFound();
            }
        });
    }

    //    public Promise<Result> create() {
    public Result create() {
        JsonNode jsonNode = request().body().asJson();
        String name = jsonNode.get("name").asText();
        return created(Json.toJson(playerService.createPlayer(name)));
//        Promise<Player> playerPromise = playerService.createPlayer(name);
//        return playerPromise.map(new F.Function<Player, Result>() {
//            @Override
//            public Result apply(Player player) throws Throwable {
//                return created(Json.toJson(player));
//            }
//        });
    }

    public Promise<Result> get(final Long id) {
        Promise<Player> playerPromise = playerService.getPlayer(id);
        return playerPromise.map(new F.Function<Player, Result>() {
            @Override
            public Result apply(Player player) throws Throwable {
                return ok(Json.toJson(player));
            }
        });
    }

    public Promise<Result> search(final String name) {
        Promise<List<Player>> playersPromise = playerService.searchPlayer(name);
        return playersPromise.map(new F.Function<List<Player>, Result>() {
            @Override
            public Result apply(List<Player> players) throws Throwable {
                if (players != null) {
                    return ok(Json.toJson(players));
                }
                return notFound();
            }
        });
    }

    public Promise<Result> update(final Long id) {
        JsonNode jsonNode = request().body().asJson();
        String name = jsonNode.get("name").asText();
        Promise<Player> playerPromise = playerService.updatePlayer(id, name);
        return playerPromise.map(new F.Function<Player, Result>() {
            @Override
            public Result apply(Player player) throws Throwable {
                if (player != null) {
                    return ok(Json.toJson(player));
                } else {
                    return notFound();
                }
            }
        });
    }

    public Promise<Result> delete(final Long id) {
        Promise<Boolean> resultPromise = playerService.deletePlayer(id);
        return resultPromise.map(new F.Function<Boolean, Result>() {
            @Override
            public Result apply(Boolean success) throws Throwable {
                if (success) {
                    return ok("Player with id " + id + " deleted successfully.");
                } else {
                    return badRequest("Could not find/delete player with id " + id);
                }
            }
        });
    }

}
