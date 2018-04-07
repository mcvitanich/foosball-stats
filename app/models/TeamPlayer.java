package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "TEAM_PLAYER")
public class TeamPlayer implements java.io.Serializable {

    @JsonIgnore
    @EmbeddedId
    private TeamPlayerId id;

    @JsonIgnore
    @ManyToOne
    @MapsId("teamId")
    private Team team;

    @ManyToOne
    @MapsId("playerId")
    private Player player;

    @Column(name = "CREATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date creationDate = new Date();

    public TeamPlayer(Team team, Player player) {
        this.player = player;
        this.team = team;
        this.id = new TeamPlayerId(team.getId(), player.getId());
    }

    public TeamPlayerId getId() {
        return id;
    }

    public void setId(TeamPlayerId id) {
        this.id = id;
    }

    @Transient
    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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

        TeamPlayer that = (TeamPlayer) o;
        return Objects.equals(player, that.player) &&
                Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, player);
    }
}
