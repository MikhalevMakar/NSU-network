package nsu.ccfit.ru.mikhalev.task3.models;

public record ResponsePlaces(String name,
                             Address address) {
    public record Address(String city,
                          String state,
                          String neighbourhood,
                          String county,
                          String suburb,
                          String country) {}
}

//"name": "Весёлые горки",
//    "address": {
//    "city": "Волгодонск",
//    "state": "РОС",
//    "county": "городской округ Волгодонск",
//    "suburb": "Старый город",
//    "country": "Россия",
//    "postcode": "347360",
//    "country_code": "ru",
//    "neighbourhood": "10"
//    },