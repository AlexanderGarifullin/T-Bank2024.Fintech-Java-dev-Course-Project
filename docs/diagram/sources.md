# Итоговая база данных
```PlantUML
@startuml

' Указываем пакеты для схем
package "security" {
    class t_users {
        +id: BIGSERIAL
        +c_name: TEXT
        +c_login: TEXT [UNIQUE]
        +c_hashed_password: TEXT
        +c_role: TEXT
    }

    class t_tokens {
        +id: BIGSERIAL
        +c_token: TEXT
        +c_revoked: BOOLEAN
        +c_user_id: BIGINT
    }
    
    t_users --> t_tokens: "1 -> N"
}

package "codeforces" {
    class t_cf_users_teams_groups {
        +id: BIGSERIAL
        +c_name: TEXT
        +c_description: TEXT
        +c_user_id: BIGINT
    }

    class t_cf_teams {
        +id: BIGSERIAL
        +c_name: TEXT
        +c_description: TEXT
        +c_first_user_login: TEXT
        +c_second_user_login: TEXT
        +c_third_user_login: TEXT
        +c_group_id: BIGINT
    }

    class t_cf_users_groups {
        +id: BIGSERIAL
        +c_name: TEXT
        +c_description: TEXT
        +c_user_id: BIGINT
    }

    class cf_users {
        +id: BIGSERIAL
        +c_name: TEXT
        +c_description: TEXT
        +c_group_id: BIGINT
    }

    t_cf_users_teams_groups --> t_cf_teams: "1 -> N"
    t_cf_users_groups --> cf_users: "1 -> N"
}

' Внешние ключи между схемами
security.t_users --> codeforces.t_cf_users_teams_groups: "1 -> N"
security.t_users --> codeforces.t_cf_users_groups: "1 -> N"

@enduml

```