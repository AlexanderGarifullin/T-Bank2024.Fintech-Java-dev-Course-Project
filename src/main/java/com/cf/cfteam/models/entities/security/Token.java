package com.cf.cfteam.models.entities.security;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "user")
@Entity
@Table(name = "t_tokens", schema = "security")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_token", nullable = false)
    private String token;

    @Column(name = "c_revoked", nullable = false)
    private boolean revoked;

    @Column(name = "c_time")
    private Instant createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_user_id", nullable = false)
    private User user;
}
