package com.cf.cfteam.models.entities.codeforces;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"teams"})
@Entity
@Table(name = "t_players", schema = "codeforces")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_login", nullable = false)
    private String login;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "t_team_player",
            joinColumns = @JoinColumn(name = "c_player_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "c_team_id", referencedColumnName = "id"))
    @Builder.Default
    private List<Team> teams = new ArrayList<>();
}
