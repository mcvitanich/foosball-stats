package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "PLAYER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

    @Id
    @Column(name = "PLAYER_ID", precision = 12, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 50)
    private String name;

//    @ManyToOne
//    @JoinColumn(name="TEAM_ID", nullable=false)
//    private Team team;

//    public Player(Team t) {
//        this.team = t;
//    }

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Player buildFromIterator(Object[] row) {
        Player player = new Player();

        Long id = getLong(row, FIELDS.PLAYER_ID);
        String name = get(row, FIELDS.NAME);

        player.setId(id);
        player.setName(name);

        return player;
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
