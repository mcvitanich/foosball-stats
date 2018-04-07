package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Match;
import models.Player;
import models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Result;
import services.MatchService;
import services.PlayerService;
import services.TeamService;

import java.util.List;

import static play.libs.Json.toJson;

@Controller
public class Matches extends play.mvc.Controller {

    @Autowired
    private MatchService matchService;

    @Autowired
    private TeamService teamService;

    public Promise<Result> getAll() {
        Promise<List<Match>> matchesPromise = matchService.getAllMatches();
        return matchesPromise.map(new F.Function<List<Match>, Result>() {
            @Override
            public Result apply(List<Match> matches) throws Throwable {
                if (matches != null) {
                    return ok(toJson(matches));
                }
                return notFound();
            }
        });
    }

    public Promise<Result> create() {
        JsonNode jsonNode = request().body().asJson();

        long teamId1 = jsonNode.get("team_id_1").asLong();
        long teamId2 = jsonNode.get("team_id_2").asLong();

        Promise<Team> team1Promise = teamService.get(teamId1);

        return team1Promise.flatMap(new F.Function<Team, Promise<Result>>() {
            @Override
            public Promise<Result> apply(Team team1) throws Throwable {

                Promise<Team> team2Promise = teamService.get(teamId2);

                return team2Promise.map(new F.Function<Team, Result>() {
                    @Override
                    public Result apply(Team team2) throws Throwable {
                        Match match = matchService.createMatch(team1, team2);
                        return created(toJson(match));
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
                    Promise<Match> matchPromise = matchService.getMatch(id);
                    return matchPromise.map(new F.Function<Match, Result>() {
                        @Override
                        public Result apply(Match match) throws Throwable {
                            if (match != null) {
                                return JPA.withTransaction((F.Function0<Result>) () -> {
                                    return ok(toJson(match));
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
        Long teamId1 = jsonNode.get("team_id_1").asLong();
        Long teamId2 = jsonNode.get("team_id_2").asLong();

        Promise<Team> team1Promise = teamService.get(teamId1);

        return team1Promise.flatMap(new F.Function<Team, Promise<Result>>() {
            @Override
            public Promise<Result> apply(Team team1) throws Throwable {

                Promise<Team> team2Promise = teamService.get(teamId2);

                return team2Promise.flatMap(new F.Function<Team, Promise<Result>>() {
                    @Override
                    public Promise<Result> apply(Team team2) throws Throwable {
                        Promise<Match> matchPromise = matchService.updateMatch(id, team1, team2);

                        return matchPromise.map(new F.Function<Match, Result>() {
                            @Override
                            public Result apply(Match match) throws Throwable {
                                return ok(toJson(match));
                            }
                        });
                    }
                });
            }
        });

    }

    public Promise<Result> delete(final Long id) {
        Promise<Boolean> resultPromise = matchService.deleteMatch(id);
        return resultPromise.map(new F.Function<Boolean, Result>() {
            @Override
            public Result apply(Boolean success) throws Throwable {
                if (success) {
                    return ok("Match with id " + id + " deleted successfully.");
                } else {
                    return badRequest("Could not find/delete match with id " + id);
                }
            }
        });
    }

}
