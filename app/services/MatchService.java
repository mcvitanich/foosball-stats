package services;

import models.Match;
import models.Player;
import models.Team;
import org.springframework.stereotype.Component;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.Logger.error;
import static play.db.jpa.JPA.withTransaction;
import static play.libs.F.Promise.promise;

@Component
public class MatchService {
    
    public Match createMatch(Team team1, Team team2) {
        try {
            return JPA.withTransaction(new F.Function0<Match>() {
                @Override
                public Match apply() throws Throwable {
                    Match match = new Match();
                    match.setStartDate(new Date());

                    match.addTeam(team1);
                    match.addTeam(team2);

                    Match newMatch = JPA.em().merge(match);
                    return newMatch;
                }
            });
        } catch (Throwable e) {
            error("An error ocurred when creating Match!", e);
            error(e.toString());
        }
        return null;
    }

    public Promise<Match> getMatch(final Long id) {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                Match match = JPA.em().find(Match.class, id);
                return match;
            } catch (Exception e) {
                Logger.error("Error trying to access database to get a match", e);
                throw e;
            }
        }));

    }

    public Promise<List<Match>> search(Long teamId) {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                return JPA.em().createQuery("SELECT m from Match m join m.matchTeams mt where mt.id.teamId="+teamId).getResultList();
            } catch (Exception e) {
                Logger.error("Error trying to access database to search matches", e);
                throw e;
            }
        }));
    }

    public Promise<Match> updateMatch(final Long id, final Team team1, final Team team2) {
        Promise<Match> promise = promise(() -> withTransaction(() -> {
            EntityManager em = JPA.em();
            Match match = em.find(Match.class, id);
            if (match != null) {

                match.getMatchTeams().clear();
                match.addTeam(team1);
                match.addTeam(team2);

                em.merge(match);
                return match;
            } else {
                return null;
            }
        }));
        return promise;
    }


    public Promise<Boolean> deleteMatch(Long id) {
        Promise<Match> matchPromise = this.getMatch(id);
        return matchPromise.map(new F.Function<Match, Boolean>() {
            @Override
            public Boolean apply(Match match) throws Throwable {
                if (match != null) {
                    return JPA.withTransaction("default", true, () -> {
                        JPA.em().remove(match);
                        return Boolean.TRUE;
                    });
                }
                return Boolean.FALSE;
            }
        });
    }

    public Promise<List<Match>> getAllMatches() {
        return promise(() -> withTransaction("default", true, () -> {
            try {
                List<Match> results = JPA.em().createQuery("SELECT m FROM Match m").getResultList();
                return results;
            } catch (Exception e) {
                Logger.error("Error trying to access database to get all matches", e);
                throw e;
            }
        }));
    }
}
