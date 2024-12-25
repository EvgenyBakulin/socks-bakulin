### База данных PostgreSQL через PgAdmin
url   jdbc:postgresql://localhost:5432/socksStorageDB
username BakulinE
password 1234
### Добавление записи о партии носков в базу:
curl --request POST - sL  \
--headers  "Content-type: application/json;
charset=utf-8"
--url http://localhost:8081/api/socks/income \
--data '{"colour": "Жёлтый","cottonContent": 44.4, "quantity":10}' \
### Выдача носков со склада:
curl --request POST - sL  \
--headers  "Content-type: application/json;
charset=utf-8"
--url http://localhost:8081/api/socks/outcome \
--data '{"colour": "Жёлтый","cottonContent": 44.4, "quantity":10}' \

### Изменение записи в базе данных:
curl --request PUT - sL  \
--url http://localhost:8081/api/socks/1 \
--data '{"colour": "Жёлтый","cottonContent": 44.4, "quantity":10}' \

### Выгрузка в базу данных из файла Excel:
curl --request POST - sL  \
--url http://localhost:8081/api/socks/batch \
 --data файл с расширением .xlsx

### Получение данных с фильтрацией:
curl --request GET - sL  \
--url http://localhost:8081/api/socks?colour=Красный?connonContent=50?moreThanCottonContent=10?lessThanCottonContent=60 \
