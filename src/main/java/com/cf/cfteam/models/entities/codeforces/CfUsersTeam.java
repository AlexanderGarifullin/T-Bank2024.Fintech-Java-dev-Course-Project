package com.cf.cfteam.models.entities.codeforces;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "group")
@Entity
@Table(name = "t_cf_teams", schema = "codeforces")
public class CfUsersTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_name", nullable = false)
    private String name;

    @Column(name = "c_description", nullable = true)
    private String description;

    @Column(name = "c_first_user_login", nullable = true)
    private String firstUser;

    @Column(name = "c_second_user_login", nullable = true)
    private String secondUser;

    @Column(name = "c_third_user_login", nullable = true)
    private String thirdUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_group_id", nullable = false)
    private CfUsersTeamsGroup group;
}
