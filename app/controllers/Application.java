package controllers;

import models.Player;
import models.Team;
import play.*;
import play.db.jpa.JPA;
import play.libs.F;
import play.mvc.*;

import views.html.*;

import java.util.HashSet;
import java.util.Set;

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

}
