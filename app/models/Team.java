package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "TEAM")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team  {

    @Id
//    @Column(name = "TEAM_ID", precision = 12, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 50)
    private String name;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamPlayer> teamPlayers = new ArrayList<>();

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchTeam> matchTeams = new ArrayList<>();

    public void addPlayer(Player player) {
        if (this.name == null || this.name.length() == 0) {
            this.name = player.getName();
        } else {
            this.name = this.name + "-" + player.getName();
        }

        TeamPlayer tp = new TeamPlayer(this, player);
        teamPlayers.add(tp);
        player.getTeamPlayers().add(tp);
    }

}
