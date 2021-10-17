# Запуск тестов #
1. Чтобы запустить контейнеры с MySql, PostgreSQL и Node.js использовать команду в терминале InteliJ Idea docker-compose up
2. Запустить SUT в MySql командой: java -jar artifacts/aqa-shop.jar -P:jdbc.url=jdbc:mysql://192.168.99.100:3306/app -P:jdbc.user=app -P:jdbc.password=pass. Либо запустить на Postgres:java -Dspring.datasource.url=jdbc:postgresql://192.168.99.100:5432/app -jar artifacts/aqa-shop.jar 
3. Запустить тесты командой: ./gradlew test
4. Чтобы получить отчет (Allure) использовать команду ./gradlew allureServe
5. Остановить контейнеры: docker-compose down.
