package com.ced.gweather.auth.features

import androidx.lifecycle.MutableLiveData
import com.ced.authentication.domain.interactor.GetUserUseCase
import com.ced.authentication.domain.interactor.RegisterUserUseCase
import com.ced.authentication.domain.model.User
import com.ced.commons.clean.interactor.Failure
import com.ced.commons.clean.rx.EmptySingleObserver
import com.ced.commons.util.log.Logger
import com.ced.gweather_core.internal.viewmodel.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


class AuthenticateViewModel
@Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : BaseViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var authenticationState = MutableLiveData<AuthenticationState>()

    private val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    // login
    var emailOrPhone = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    // Sign-up
    var regName = MutableLiveData<String>()
    var regEmail = MutableLiveData<String>()
    var regPass = MutableLiveData<String>()

    var showSignupSection = MutableLiveData(false)
    var isCreatingUserFinished = MutableLiveData(false)

    var user: User? = null

    init {
        addUseCases {
            add(getUserUseCase)
        }

/*        if (BuildConfig.DEBUG) {
            emailOrPhone.value = "ced@gmail.com"
            password.value = "asdasd"
        }*/
    }

    fun isLoginFormValid(): Boolean {
        return !emailOrPhone.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
    }

    fun isRegFormValid(): Boolean {
        return regEmail.value?.isNotEmpty() == true && regPass.value?.isNotEmpty() == true
                && regName.value?.isNotEmpty() == true
    }

    fun login() {
        firebaseAuth.signInWithEmailAndPassword(emailOrPhone.value!!, password.value!!)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser == null) {
                        handleFailure(Failure.ServerError)
                        authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                    } else {
                        if (firebaseUser.email != null) {
                            loginToGWeather(firebaseUser.email!!)
                        } else {
                            handleFailure(Failure.ServerError)
                            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                        }
                    }

                } else {
                    authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                }
            }
            .addOnFailureListener { e ->
                if (e is FirebaseAuthInvalidCredentialsException) {
                    handleFailure(FailedToLoginUser(e, customErrorMsg = null))
                } else if (e is FirebaseAuthInvalidUserException) {
                    val errorCode = e.errorCode
                    if (errorCode == "ERROR_USER_NOT_FOUND") {
                        handleFailure(FailedToLoginUser(customErrorMsg = "No matching account found."))
                    }
                    if (errorCode == "ERROR_USER_DISABLED") {
                        handleFailure(FailedToLoginUser(customErrorMsg = "User account has been disabled"))
                    }
                }
            }
    }

    fun createNewAccount() {
        firebaseAuth.createUserWithEmailAndPassword(regEmail.value!!, regPass.value!!)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    User().run {
                        id = user?.uid
                        email = user?.email
                        name = regName.value
                        dateCreated = Calendar.getInstance().time

                        registerUserUseCase.execute(
                            object : EmptySingleObserver<User>() {
                                override fun onSuccess(result: User) {
                                    isCreatingUserFinished.value = true

                                    showSignupSection.value = false
                                }
                            }, this
                        )
                    }
                } else {
                    authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                }
            }
            .addOnFailureListener { e ->
                handleFailure(FailedToRegisterUser(e))
            }
    }

    private fun loginToGWeather(email: String) {
        getUserUseCase.execute(object : EmptySingleObserver<User>() {
            override fun onSuccess(result: User) {
                user = result

                if (authenticationState.value != AuthenticationState.AUTHENTICATED) {
                    authenticationState.value = AuthenticationState.AUTHENTICATED
                }
            }

            override fun onError(throwable: Throwable) {
                Logger.d(TAG, "User detail loading failed: ${throwable.message}", throwable)

                handleFailure(Failure.ServerError)
                authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
            }
        }, email)
    }

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,        // The user has authenticated successfully
        INVALID_AUTHENTICATION,  // Authentication failed
    }

    companion object {
        private val TAG = AuthenticateViewModel::class.java.simpleName
    }
}