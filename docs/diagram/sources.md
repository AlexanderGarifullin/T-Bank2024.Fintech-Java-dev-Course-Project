# Итоговая база данных
```PlantUML
@startuml

package "security" {
    class t_users {
        +id: BIGSERIAL
        +c_name: TEXT [NOT NULL]
        +c_login: TEXT [UNIQUE NOT NULL]
        +c_hashed_password: TEXT [NOT NULL]
        +c_role: TEXT [NOT NULL]
    }

    class t_tokens {
        +id: BIGSERIAL
        +c_token: TEXT [NOT NULL]
        +c_revoked: BOOLEAN [NOT NULL]
        +c_user_id: BIGINT [NOT NULL]
    }
    
    t_users --> t_tokens: "1 -> N"
}

package "codeforces" {
    class t_groups {
        +id: BIGSERIAL
        +c_name: TEXT [NOT NULL]
        +c_description: TEXT [NULL]
        +c_user_id: BIGINT [NOT NULL]
    }

    class t_teams {
      +id: BIGSERIAL
      +c_name: TEXT [NOT NULL]
      +c_description: TEXT [NULL]
      +c_group_id: BIGINT [NOT NULL]
    }
    
    class t_players {
      +id: BIGSERIAL
      +c_login: TEXT [UNIQUE NOT NULL]
    }
    
    class t_team_player {
      +id: BIGSERIAL
      +c_team_id: BIGINT [NOT NULL]
      +c_player_id: BIGINT [NOT NULL]
    }
}

security.t_users --> codeforces.t_groups: "1 -> N"
codeforces.t_groups --> codeforces.t_teams: "1 -> N"
codeforces.t_teams --> codeforces.t_team_player: "1 -> N"
codeforces.t_players --> codeforces.t_team_player: "1 -> N"
@enduml


```