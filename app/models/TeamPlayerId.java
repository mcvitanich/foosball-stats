package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class TeamPlayerId implements java.io.Serializable {

    @Column(name = "TEAM_ID")
    private Long teamId;

    @Column(name = "PLAYER_ID")
    private Long playerId;

    private TeamPlayerId() {}

    public TeamPlayerId(Long teamId, Long playerId) {
        this.playerId = playerId;
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        TeamPlayerId that = (TeamPlayerId) o;
        return Objects.equals(playerId, that.playerId) &&
                Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, playerId);
    }
}
