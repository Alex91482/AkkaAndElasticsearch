# AkkaAndElasticsearch

- curl -H "Content-Type: application/json" -X POST -d '{"myTestEntity":[{"id":"42","name":"test_name","surname":"test_surname","description":"test_description","parameter":["test","test1","test2"]}]}' http://localhost:8080/create_myTestEntity
- curl like: http://localhost:8080/myTestEntity/42
- curl http://localhost:8080/myTestEntity/42.

### Конечные точки

- /create_myTestEntity - (POST) сохранить сущность MyTestEntity в бд
- /myTestEntity/{id}   - (GET) где {id} это id запрашиваемой сущности MyTestEntity

### Ресурсы

- /static/js/{name}    - (GET) запрос скриптов JS где {name} это имя файла скрипта
- /static/css/{name}   - (GET) запрос файлов css где {name} это имя файла стилей

### Адреса страниц

- /index
 
