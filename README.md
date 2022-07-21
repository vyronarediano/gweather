
# GWeather

A beautiful and minimal weather app illustrating Android development best practices that displays the current weather forecast of the city to the user using the current location.

![gweather-gif](https://user-images.githubusercontent.com/17285933/180107895-ef86a164-b0bc-4788-848f-101a4ebb0e52.gif)





## Features

- Modular Android App Architecture
- MVVM + Clean Architecture
- Secure Authentication by Google Authentication
- Login/Registration & Cloud Storage by Google Cloud Firestore
- Displays Current Weather Data
- Weather Records (Fetched data and stored in DB when user opens the app only)


## Tech Stack

**Language:** Kotlin

**Architecture:** MVVM [Model-View-ViewModel] + Clean architecture

**Dependency Injection:** Dagger

**REST API:** Retrofit

**Database:** Google Firebase Firestore

**Authentication:** Google Authentication

**Others:** Android Jetpack, RxJava, RxKotlin, Espresso, LiveData



## Getting Started

To be able to get the data from OpenWeatherApp, you need to register to their website to get the API KEY

#### Get current weather API

Once you already have the API key, go to **SessionMemoryRepository** class and paste your API_KEY


```kotlin
  sharedPreferences.getString(KEY_OPEN_WEATHER_MAP, "YOUR_API_KEY")
```



## Acknowledgements
- [Google Firebase](https://firebase.google.com/)
- [Google Fonts & Icons](https://fonts.google.com/)
- [LottieFiles](https://lottiefiles.com/)
- [Designevo - Free Logo](https://www.designevo.com/)
- [README Editor](https://readme.so/)


## Feedback

If you have any feedback, please reach out to us at vyronarediano@gmail.com
