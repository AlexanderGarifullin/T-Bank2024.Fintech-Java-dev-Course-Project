package com.cf.cfteam.models.entities.codeforces;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "group")
@Entity
@Table(name = "cf_users", schema = "codeforces")
public class CfUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_name", nullable = false)
    private String name;

    @Column(name = "c_description", nullable = true)
    private String description;

    @Column(name = "с_time", nullable = false)
    private Instant createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_group_id", nullable = false)
    private CfUsersGroup group;
}
