package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Player;
import models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Result;
import services.PlayerService;
import services.TeamService;

import java.util.List;

import static play.libs.Json.toJson;

@Controller
public class Teams extends play.mvc.Controller {

    @Autowired
    private TeamService teamsService;

    @Autowired
    private PlayerService playerService;

    public Promise<Result> getAll() {
        Promise<List<Team>> teamsPromise = teamsService.getAll();
        return teamsPromise.map(new F.Function<List<Team>, Result>() {
            @Override
            public Result apply(List<Team> teams) throws Throwable {
                if (teams != null) {
                    return ok(toJson(teams));
                }
                return notFound();
            }
        });
    }

    //    public Promise<Result> create() {
    public Promise<Result> create() {
        JsonNode jsonNode = request().body().asJson();
        String name = jsonNode.get("name").asText();

        long player1_id = jsonNode.get("player1_id").asLong();
        long player2_id = jsonNode.get("player2_id").asLong();

        Promise<Player> player1Promise = playerService.getPlayer(player1_id);

        return player1Promise.flatMap(new F.Function<Player, Promise<Result>>() {
            @Override
            public Promise<Result> apply(Player player1) throws Throwable {

                Promise<Player> player2Promise = playerService.getPlayer(player2_id);

                return player2Promise.map(new F.Function<Player, Result>() {
                    @Override
                    public Result apply(Player player2) throws Throwable {
                        Team team = teamsService.create(name, player1, player2);
                        return created(toJson(team));
                    }
                });
            }
        });

    }

    public Promise<Result> get(final Long id) {
        try {
            return JPA.withTransaction(new F.Function0<Promise<Result>>() {
                @Override
                public Promise<Result> apply() throws Throwable {
                    Promise<Team> teamPromise = teamsService.get(id);
                    return teamPromise.map(new F.Function<Team, Result>() {
                        @Override
                        public Result apply(Team team) throws Throwable {
                            if (team != null) {

                                return JPA.withTransaction((F.Function0<Result>) () -> {
                                    return ok(toJson( team));
                                });
                            } else {
                                return notFound();
                            }
                        }
                    });
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Promise.pure(internalServerError());
    }

//    public Promise<Result> search(final String name) {
//        Promise<List<Player>> playersPromise = teamsService.search(name);
//        return playersPromise.map(new F.Function<List<Player>, Result>() {
//            @Override
//            public Result apply(List<Player> players) throws Throwable {
//                if (players != null) {
//                    return ok(Json.toJson(players));
//                }
//                return notFound();
//            }
//        });
//    }

    public Promise<Result> update(final Long id) {
        JsonNode jsonNode = request().body().asJson();
        String name = jsonNode.get("name").asText();
        Long player1_id = jsonNode.get("player1_id").asLong();
        Long player2_id = jsonNode.get("player2_id").asLong();

        Promise<Player> player1Promise = playerService.getPlayer(player1_id);

        return player1Promise.flatMap(new F.Function<Player, Promise<Result>>() {
            @Override
            public Promise<Result> apply(Player player1) throws Throwable {

                Promise<Player> player2Promise = playerService.getPlayer(player2_id);

                return player2Promise.flatMap(new F.Function<Player, Promise<Result>>() {
                    @Override
                    public Promise<Result> apply(Player player2) throws Throwable {
                        Promise<Team> teamPromise = teamsService.update(id, name, player1, player2);

                        return teamPromise.map(new F.Function<Team, Result>() {
                            @Override
                            public Result apply(Team team) throws Throwable {
                                return ok(toJson(team));
                            }
                        });
                    }
                });
            }
        });

    }

    public Promise<Result> delete(final Long id) {
        Promise<Boolean> resultPromise = teamsService.delete(id);
        return resultPromise.map(new F.Function<Boolean, Result>() {
            @Override
            public Result apply(Boolean success) throws Throwable {
                if (success) {
                    return ok("Team with id " + id + " deleted successfully.");
                } else {
                    return badRequest("Could not find/delete team with id " + id);
                }
            }
        });
    }

}
