
# GWeather [Work in progress 🚧]

A beautiful and minimal weather app illustrating Android development best practices that displays the current weather forecast of the city to the user using the current location.





## Features

- Modular Android app architecture
- MVVM + Clean
- Secure Authentication & Login/Registration using Cloud Firestore
- Current Weather Data
- List of Weather fetched


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