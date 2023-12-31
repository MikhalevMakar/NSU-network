# Асинхронное сетевое взаимодействие

Это описание задачи по созданию приложения, которое использует асинхронное сетевое взаимодействие с несколькими публично доступными API.

## Цель

Создать приложение с пользовательским интерфейсом, используя методы асинхронного программирования (например, CompletableFuture для Java) или библиотеки реактивного программирования (например, RxJava), которое взаимодействует с несколькими публично доступными API.

## Логика работы

1. Пользователь вводит название локации (например, "Цветной проезд") в поле ввода и нажимает кнопку "Поиск".


2. Приложение выполняет асинхронные HTTP-запросы к нескольким API и работает следующим образом:
    - Выполняются асинхронные запросы ко всем API одновременно, без блокировок для ожидания промежуточных результатов.
    - Если какие-то вызовы API зависят от данных, полученных из предыдущих вызовов, они оформляются в виде асинхронной цепочки вызовов.
    - Ожидание результатов разрешено только при ожидании конечного результата (например, в консольном приложении).
    - Вся логика программы оформлена как одна функция, возвращающая CompletableFuture (или аналог в выбранном языке программирования).
    

3. Результаты запросов API показываются пользователю в приложении.

## Методы API

1. Получение локаций с координатами и названиями:
    - [getGeocode](https://docs.graphhopper.com/#operation/getGeocode)


2. Получение погоды по координатам:
    - [OpenWeatherMap](https://openweathermap.org/current)


3. Получение списка интересных мест по координатам:
    - [OpenTripMap](https://opentripmap.io/docs#/Objects%20list/getListOfPlacesByRadius)


4. Получение описания места по его id:
    - [OpenTripMap](https://opentripmap.io/docs#/Object%20properties/getPlaceByXid)

