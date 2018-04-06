package models;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "MATCH_TEAM")
public class MatchTeam implements java.io.Serializable {

    @EmbeddedId
    private MatchTeamId id;

    @ManyToOne
    @MapsId("matchId")
    private Match match;

    @ManyToOne
    @MapsId("teamId")
    private Team team;

    @Column(name = "CREATION_DATE")
    private Date creationDate = new Date();

    private MatchTeam() {}

    public MatchTeam(Match match, Team team) {
        this.match = match;
        this.team = team;
        this.id = new MatchTeamId(match.getId(), team.getId());
    }

    public MatchTeamId getId() {
        return id;
    }

    public void setId(MatchTeamId id) {
        this.id = id;
    }

    @Transient
    public Match getMatch() {
        return this.match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Transient
    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MatchTeam that = (MatchTeam) o;
        return Objects.equals(match, that.match) &&
                Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match, team);
    }

}
