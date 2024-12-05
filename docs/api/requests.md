# POST (create)
## user
1. /auth/register (user): зарегистрировать пользователя.
2. /auth/login (user): вход в приложение. 
3. /auth/logout (user): выход из приложения.

## cf user
1. /cf/user (cf_user): добавить пользователя с платформы codeforces.

## cf users team
1. /cf/teams (team): добавить команду пользователей платформы codeforces.

## groups
1. /groups/solo (group): создать группу cf пользователей.
2. /groups/team (group): создать группу команд cf пользователей.

# GET (read)
## cf user
1. /cf/user (id): получить пользователя с платформы codeforces по id в нашей БД.

## cf users team
1. /cf/teams (id): получить команду пользователей платформы codeforces  по id в нашей БД.

## groups
1. /groups/solo (id): получить группу cf пользователей  по id в нашей БД.
2. /groups/team (id): получить группу команд cf пользователей по id в нашей БД.

# PUT (update)

## cf user
1. /cf/user (cf_user): обновить пользователя с платформы codeforces.

## cf users team
1. /cf/teams (team): обновить команду пользователей платформы codeforces.

## groups
1. /groups/solo (group): овить группу cf пользователей.
2. /groups/team (group): обновить группу команд cf пользователей.бно

# DELETE (delete)

1. /auth/{id}: удалить пользователя под его id + удалить все его группы.

## cf user
1. /cf/user (id): удалить пользователя с платформы codeforces.

## cf users team
1. /cf/teams (id): удалить команду пользователей платформы codeforces.

## groups
1. /groups/solo (id): удалить группу cf пользователей.
2. /groups/team (id): удалить группу команд cf пользователей.бно

