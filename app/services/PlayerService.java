package services;

import models.Match;
import models.Player;
import org.springframework.stereotype.Component;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static play.Logger.error;
import static play.db.jpa.JPA.em;
import static play.db.jpa.JPA.withTransaction;
import static play.libs.F.Promise.promise;

@Component
public class PlayerService {

    private final static String GET_PLAYER_QUERY = "select player_id, name from player where player_id = %d";

    private final static String GET_ALL_PLAYERS = "select player_id, name from player";

    private final static String GET_PLAYER_BY_NAME_QUERY = "select player_id, name from player where LOWER(name) like '%%%s%%'";


    public Player createPlayer(String name) {
        try {
            return JPA.withTransaction(new F.Function0<Player>() {
                @Override
                public Player apply() throws Throwable {
                    Player player = new Player();
                    player.setName(name);
                    Player playerSaved = JPA.em().merge(player);
                    return playerSaved;
                }
            });
        } catch (Throwable e) {
            error("An error ocurred when creating Player!");
        }
        return null;
    }


    public Promise<Player> getPlayer(final Long id) {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                Player player = JPA.em().find(Player.class, id);
                return player;
            } catch (Exception e) {
                Logger.error("Error trying to access database to get a player", e);
                throw e;
            }
        }));
    }

    public Promise<List<Player>> searchPlayer(final String name) {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                List<Player> resultList = new ArrayList<>();
                String query = format(GET_PLAYER_BY_NAME_QUERY, name.toLowerCase());
                Iterator rows = JPA.em().createNativeQuery(query).getResultList().iterator();
                if (!rows.hasNext()) {
                    return null;
                }
                while (rows.hasNext()) {
                     resultList.add(Player.buildFromIterator((Object[]) rows.next()));
                }
                return resultList;
            } catch (Exception e) {
                Logger.error("Error trying to access database", e);
                throw e;
            }
        }));
    }

    public Promise<Player> updatePlayer(final Long id, final String name) {
        Promise<Player> promise = promise(() -> withTransaction(() -> {
            EntityManager em = JPA.em();
            Player player = em.find(Player.class, id);
            if (player != null) {
                player.setName(name);
                em.merge(player);
                return player;
            } else {
                return null;
            }
        }));
        return promise;
    }

    public Promise<Boolean> deletePlayer(Long id) {
        Promise<Player> playerPromise = this.getPlayer(id);
        return playerPromise.map(new F.Function<Player, Boolean>() {
            @Override
            public Boolean apply(Player player) throws Throwable {
                if (player != null) {
                    return JPA.withTransaction("default", true, () -> {
                        JPA.em().remove(player);
                        return Boolean.TRUE;
                    });
                }
                return Boolean.FALSE;
            }
        });
    }

    public Promise<List<Player>> getAllPlayers() {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                return JPA.em().createQuery("SELECT p from Player p").getResultList();
            } catch (Exception e) {
                Logger.error("Error trying to access database to get all players", e);
                throw e;
            }
        }));
    }
}
