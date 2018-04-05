package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "TEAM")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    @Id
    @Column(name = "TEAM_ID", precision = 12, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 50)
    private String name;

//    @OneToMany(mappedBy="team")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TEAM_ID")
    private List<Player> players = new ArrayList<>();

    public Team(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Team buildFromIterator(Object[] row) {
        Team campaign = new Team();

        Long id = getLong(row, FIELDS.PLAYER_ID);
        String name = get(row, FIELDS.NAME);

        campaign.setId(id);
        campaign.setName(name);

        return campaign;
    }


    private static <T> T get(Object[] columns, FIELDS field) {
        return (T) columns[field.ordinal()];
    }

    private static Long getLong(Object[] columns, FIELDS fieldj) {
        return Long.valueOf(columns[fieldj.ordinal()].toString());
    }

    enum FIELDS {
        PLAYER_ID, NAME
    }
}
