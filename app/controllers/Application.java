package controllers;

import models.Match;
import models.MatchTeam;
import models.Player;
import models.Team;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Date;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result ping() {
        return ok("pong");
    }

    public static Result test() {
        try {
            JPA.withTransaction(new F.Function0<Void>() {
                @Override
                public Void apply() throws Throwable {

                    Team t1 = new Team();
                    t1.setName("Prueba 2");

                    Player p1 = new Player();
                    p1.setName("Jugador 3");

                    Player p2 = new Player();
                    p2.setName("Jugador 4");

                    t1.getPlayers().add(p1);
                    t1.getPlayers().add(p2);

                    JPA.em().merge(t1);

                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return internalServerError();
        }

        return ok("TEST OK");
    }


    public static Result test2() {
        try {
            JPA.withTransaction(new F.Function0<Void>() {
                @Override
                public Void apply() throws Throwable {

                    Team team = JPA.em().find(Team.class, new Long(2));

                    System.out.println("TEAM FOUND: " + team);

                    if (team != null) {
                        for (Player player: team.getPlayers()) {
                            System.out.println("P FOUND: " + player);

                        }
                    }

                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return internalServerError();
        }

        return ok("TEST OK");
    }

    public static Result test3() {
        try {
            JPA.withTransaction(new F.Function0<Void>() {
                @Override
                public Void apply() throws Throwable {

                    // TEAM 1

                    Team t1 = new Team();
                    t1.setName("Equipo 1");

                    Player p1 = new Player();
                    p1.setName("Jugador 11");

                    Player p2 = new Player();
                    p2.setName("Jugador 12");

                    t1.getPlayers().add(p1);
                    t1.getPlayers().add(p2);

                    JPA.em().merge(t1);

                    // TEAM 2

                    Team t2 = new Team();
                    t2.setName("Equipo 2");

                    Player p3 = new Player();
                    p3.setName("Jugador 21");

                    Player p4 = new Player();
                    p4.setName("Jugador 22");

                    t2.getPlayers().add(p3);
                    t2.getPlayers().add(p4);

                    JPA.em().merge(t2);

                    return null;
                }
            });

            JPA.withTransaction(new F.Function0<Void>() {
                @Override
                public Void apply() throws Throwable {

                    Team t1 = JPA.em().find(Team.class, new Long(1));
                    Team t2 = JPA.em().find(Team.class, new Long(2));

                    // MATCH
                    Match m1 = new Match();
                    m1.setStartDate(new Date());
//                    m1.setId(1L);

                    m1.addTeam(t1);
                    m1.addTeam(t2);

                    JPA.em().persist(m1);

                    return null;
                }
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return internalServerError();
        }

        return ok("TEST OK");
    }


    public static Result test4() {
        try {
            JPA.withTransaction(new F.Function0<Void>() {
                @Override
                public Void apply() throws Throwable {

                    Match match = JPA.em().find(Match.class, new Long(2));

                    System.out.println("MATCH FOUND: " + Json.toJson(match));

                    if (match != null) {
                        for (MatchTeam mt: match.getMatchTeams()) {
                            Team t = mt.getTeam();
                            System.out.println("MT TEAM FOUND: " + play.libs.Json.toJson(t));
                        }
                    }

                    return null;
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return internalServerError();
        }

        return ok("TEST OK");
    }

}
