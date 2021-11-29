# Примеры доступа к REST интерфейсу приложения
## Получить список питания зарегистрированного пользователя
Все примеры предназначены для запуска в ОС MS Windows, в утилите командной строки
* curl --location --request GET http://localhost:8080/topjava/rest/meals
## Получить список питания зарегистрированного пользователя с фильтрацией по дате, времени
* curl --location --request GET http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30^&startTime=10:00^&endDate=2020-01-30^&endTime=23:01
## Добавить запись в список питания зарегистрированного
* curl --location --request POST http://localhost:8080/topjava/rest/meals --header "Content-Type: application/json" --data-raw "{""dateTime"": ""2020-01-30T20:04:00"", ""description"": ""breakfast"", ""calories"": 501, ""user"": null }"
## Изменить запись в списке питания зарегистрированного пользователя
* curl --location --request PUT http://localhost:8080/topjava/rest/meals/100002 --header "Content-Type: application/json" --data-raw "{ ""id"": 100002, ""dateTime"": ""2020-01-30T10:00:00"", ""description"": ""updated"", ""calories"": 501, ""user"": null }"
## Удалить запись в списке питания зарегистрированного пользователя
* curl --location --request DELETE http://localhost:8080/topjava/rest/meals/100002 --header "Content-Type: application/json"