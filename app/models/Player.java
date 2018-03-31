package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "PLAYER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@SequenceGenerator(name = "CampaignSequence", sequenceName = "seq_campaign_id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

    @Id
    @Column(name = "PLAYER_ID", precision = 12, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 50)
    private String name;

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Player buildFromIterator(Object[] row) {
        Player campaign = new Player();

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
