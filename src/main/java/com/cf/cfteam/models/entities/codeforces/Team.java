package com.cf.cfteam.models.entities.codeforces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"group", "players"})
@Entity
@Table(name = "t_teams", schema = "codeforces")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_name", nullable = false)
    private String name;

    @Column(name = "c_description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_group_id", nullable = false)
    private Group group;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "t_team_player", schema = "codeforces",
            joinColumns = @JoinColumn(name = "c_team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "c_player_id", referencedColumnName = "id"))
    @Builder.Default
    private List<Player> players = new ArrayList<>();
}
