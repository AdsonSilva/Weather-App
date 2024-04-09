package com.example.weatherapp.model.dtos


enum class RequestType(val ref: String) {
    CITY("City"),
    LAT_LON("LatLon"),
    IP("IP"),
    ZIPCODE("Zipcode")
}

enum class Unit(val ref: String) {
    METRIC("m"),
    SCIENTIFIC("s"),
    FAHRENHEIT("f")
}