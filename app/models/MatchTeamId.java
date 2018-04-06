package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class MatchTeamId implements java.io.Serializable {

    @Column(name = "MATCH_ID")
    private Long matchId;

    @Column(name = "TEAM_ID")
    private Long teamId;

    private MatchTeamId() {}

    public MatchTeamId(Long matchId, Long teamId) {
        this.matchId = matchId;
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MatchTeamId that = (MatchTeamId) o;
        return Objects.equals(matchId, that.matchId) &&
                Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, teamId);
    }
}
