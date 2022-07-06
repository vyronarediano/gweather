package com.ced.gweather.auth.features

import com.ced.commons.clean.interactor.Failure
import com.ced.commons.clean.interactor.InvalidArgumentException

abstract class AuthenticateUseCaseFailure(var feature: String, throwable: Throwable) :
    Failure.FeatureFailure() {

    var message: String = "Sorry, we encountered an error performing the action."

    init {
        if (throwable is InvalidArgumentException) message =
            (throwable).messages?.values?.joinToString("\n").toString()
    }

}

class FailedToRegisterUser(throwable: Throwable) :
    AuthenticateUseCaseFailure("Register User Failed", throwable)



