package com.ced.gweather.auth.features

import com.ced.commons.clean.interactor.Failure

abstract class AuthenticateUseCaseFailure(customErrorMsg: String, throwable: Throwable) :
    Failure.FeatureFailure() {

    var message: String = throwable.message.toString()
        .ifEmpty { "Sorry, we encountered an error performing the action." }

    var customErrorMessage: String = customErrorMsg
}

class FailedToRegisterUser(throwable: Throwable) :
    AuthenticateUseCaseFailure("Create New User Failed", throwable)

class FailedToLoginUser(throwable: Throwable? = null, customErrorMsg: String? = null) :
    AuthenticateUseCaseFailure(customErrorMsg.orEmpty() , throwable ?: Throwable())



