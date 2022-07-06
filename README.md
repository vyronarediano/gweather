
# GWeather

A beautiful and minimal weather app that displays the current weather forecast of the city to the user using the current location.





## Features

- Authentication
- Current Weather Data
- List of Weather Record


## Tech Stack

**Language:** Kotlin

**Architecture:** MVVM [Model-View-ViewModel] + Clean architecture

**Dependency Injection:** Dagger

**API:** Retrofit

**Database & Authentication:** Google Firebase Firestore

**Others:** Android Jetpack, RxJava, RxKotlin, Espresso, LiveData



## Getting Started

To be able to get the data from OpenWeatherApp, you need to register to their website to get the API KEY

#### Get current weather API

Once you already have the API key, go to **WeatherApi** interface and paste your API_KEY

```http
  GET data/2.5/weather?&units=metric&appid={YOUR_API_KEY}
```


