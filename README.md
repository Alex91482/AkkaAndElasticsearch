# AkkaAndElasticsearch

- curl -H "Content-Type: application/json" -X POST -d '{"id":42,"name":"test_name","surname":"test_surname","description":"test_description","parameter":["test1","test2","test3"]}' http://localhost:8080/create
- curl like: http://localhost:8080/myTestEntity/42
- curl http://localhost:8080/myTestEntity/42.

### Конечные точки

- /create              - (POST) сохранить сущность MyTestEntity в бд
- /entity/{id}         - (GET) где {id} это id запрашиваемой сущности MyTestEntity

### Ресурсы

- /static/js/{name}    - (GET) запрос скриптов JS где {name} это имя файла скрипта
- /static/css/{name}   - (GET) запрос файлов css где {name} это имя файла стилей

### Адреса страниц

- /index
 
