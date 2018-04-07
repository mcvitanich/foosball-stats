package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "GOAL")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Goal {

    @Id
    @Column(name = "GOAL_ID", precision = 12, columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @Column(name = "EVENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date eventDate;

}
