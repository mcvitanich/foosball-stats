package services;

import models.Player;
import models.Team;
import org.springframework.stereotype.Component;
import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static play.Logger.error;
import static play.db.jpa.JPA.withTransaction;
import static play.libs.F.Promise.promise;

@Component
public class TeamService {

    private final static String GET_TEAM_QUERY = "select * from team where team_id = %d";

    private final static String GET_ALL_TEAMS = "select * from team";

//    private final static String GET_TEAM_BY_NAME_QUERY = "select player_id, name from player where LOWER(name) like '%%%s%%'";


    public Team create(String name, Player p1, Player p2) {
        try {
            return JPA.withTransaction(new F.Function0<Team>() {
                @Override
                public Team apply() throws Throwable {
                    Team team = new Team();
                    team.setName(name);
                    List<Player> players = new ArrayList<>();
                    players.add(p1);
                    players.add(p2);
                    team.setPlayers(players);
                    Team teamSaved = JPA.em().merge(team);
                    return teamSaved;
                }
            });
        } catch (Throwable e) {
            error("An error ocurred when creating Team!");
        }
        return null;
    }

    public Promise<Team> get(final Long id) {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                Team team = JPA.em().find(Team.class, id);
                return team;
            } catch (Exception e) {
                Logger.error("Error trying to access database", e);
                throw e;
            }
        }));

    }

//    public Promise<List<Player>> search(final String name) {
//        return promise(() -> withTransaction("default", true, () -> {
//            try {
//                List<Player> resultList = new ArrayList<>();
//                String query = format(GET_PLAYER_BY_NAME_QUERY, name.toLowerCase());
//                Iterator rows = JPA.em().createNativeQuery(query).getResultList().iterator();
//                if (!rows.hasNext()) {
//                    return null;
//                }
//                while (rows.hasNext()) {
//                     resultList.add(Player.buildFromIterator((Object[]) rows.next()));
//                }
//                return resultList;
//            } catch (Exception e) {
//                Logger.error("Error trying to access database", e);
//                throw e;
//            }
//        }));
//    }

    public Promise<Team> update(final Long id, final String name, final Player player1, final Player player2) {
        Promise<Team> promise = promise(() -> withTransaction(() -> {
            EntityManager em = JPA.em();
            Team team = em.find(Team.class, id);
            if (team != null) {
                team.setName(name);

                List<Player> players = new ArrayList<>();
                players.add(player1);
                players.add(player2);
                team.setPlayers(players);

                em.merge(team);
                return team;
            } else {
                return null;
            }
        }));
        return promise;
    }


    public Promise<Boolean> delete(Long id) {
        Promise<Team> teamPromise = this.get(id);
        return teamPromise.map(new F.Function<Team, Boolean>() {
            @Override
            public Boolean apply(Team team) throws Throwable {
                if (team != null) {
                    return JPA.withTransaction("default", true, () -> {
                        JPA.em().remove(team);
                        return Boolean.TRUE;
                    });
                }
                return Boolean.FALSE;
            }
        });
    }

    public Promise<List<Team>> getAll() {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                return JPA.em().createQuery("SELECT t FROM Team t").getResultList();
            } catch (Exception e) {
                Logger.error("Error trying to access database", e);
                throw e;
            }
        }));
    }
}
