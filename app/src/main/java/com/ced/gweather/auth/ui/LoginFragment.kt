package com.ced.gweather.auth.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.ced.commons.clean.interactor.Failure
import com.ced.commons.ui.extensions.gone
import com.ced.commons.ui.extensions.invisible
import com.ced.commons.ui.extensions.viewModel
import com.ced.commons.ui.extensions.visible
import com.ced.commons.ui.observe
import com.ced.commons.util.DeviceManager
import com.ced.commons.util.log.Logger
import com.ced.gweather.R
import com.ced.gweather.auth.features.AuthenticateUseCaseFailure
import com.ced.gweather.auth.features.AuthenticateViewModel
import com.ced.gweather.auth.features.AuthenticateViewModel.AuthenticationState
import com.ced.gweather.auth.features.FailedToLoginUser
import com.ced.gweather.auth.features.FailedToRegisterUser
import com.ced.gweather.databinding.LoginFragmentBinding
import com.ced.gweather.weather.ui.BaseFragmentDI
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragmentDI() {

    private lateinit var authenticateViewModel: AuthenticateViewModel

    private lateinit var binding: LoginFragmentBinding

    override fun layoutId(): Int = R.layout.login_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        this.binding = DataBindingUtil.inflate(layoutInflater, layoutId(), container, false)
        this.binding.lifecycleOwner = this

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appComponent.inject(this)

        authenticateViewModel = viewModel(viewModelFactory) {
            observe(authenticationState, ::handleAuthenticationState)
            observe(showSignupSection, ::showRegForm)
            observe(isCreatingUserFinished, ::onCreateUserFinished)
            observe(failure, ::handleFailure)
        }

        binding.authenticateViewModel = authenticateViewModel

        btnLoginSubmit.setOnClickListener {
            validate(DeviceManager.isNetworkAvailable(requireContext()))
        }

        btnRegSubmit.setOnClickListener {
            createNewAccount(DeviceManager.isNetworkAvailable(requireContext()))
        }

        btnGoToSignupView.setOnClickListener {
            authenticateViewModel.showSignupSection.value = true
        }

        btnGoToLoginView.setOnClickListener {
            authenticateViewModel.showSignupSection.value = false
        }
    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }


    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Logger.i(TAG, "Displaying permission rationale to provide additional context.")

            showSnackBar(
                "Location permission is needed for core functionality",
                "Okay",
                Snackbar.LENGTH_LONG,
                view?.rootView,
                onActionCallback = {
                    startLocationPermissionRequest()
                })
        } else {
            Logger.i(TAG, "Requesting permission")

            startLocationPermissionRequest()
        }
    }


    private fun toggleProgressBarVisibility(show: Boolean) {
        if (show) {
            progressBar.visible()
            progressBar.progress = 20

            btnLoginSubmit.isEnabled = false
            btnRegSubmit.isEnabled = false
            btnGoToSignupView.isEnabled = false
            btnGoToLoginView.isEnabled = false

            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

        } else {
            progressBar.invisible()
            progressBar.progress = 100

            btnLoginSubmit.isEnabled = true
            btnRegSubmit.isEnabled = true
            btnGoToSignupView.isEnabled = true
            btnGoToLoginView.isEnabled = true

            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    /**
     * When user taps the LOG IN button
     */
    private fun validate(online: Boolean) {
        toggleProgressBarVisibility(true)
        updateLoginErrorMessage(null)
        hideKeyboard()

        if (!online) {
            toggleProgressBarVisibility(false)
            updateLoginErrorMessage(resources.getString(R.string.online_required))
            return
        }

        if (authenticateViewModel.isLoginFormValid() && online) {
            authenticateViewModel.login()
        } else {
            updateLoginErrorMessage(resources.getString(R.string.login_provide_email_pass))
            toggleProgressBarVisibility(false)
        }
    }

    /**
     * When user taps the CREATE NEW ACCOUNT button
     */
    private fun createNewAccount(online: Boolean) {
        toggleProgressBarVisibility(true)
        updateRegErrorMessage(null)

        if (!online) {
            toggleProgressBarVisibility(false)
            updateRegErrorMessage(resources.getString(R.string.online_required))
            return
        }

        if (authenticateViewModel.isRegFormValid() && online) {
            authenticateViewModel.createNewAccount()
        } else {
            updateRegErrorMessage("Please supply all the fields.")
            toggleProgressBarVisibility(false)
        }
    }

    private fun showRegForm(show: Boolean?) {
        val slideInRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val slideOutLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)

        val slideInLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
        val slideOutRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)

        if (show == true) {
            layoutLoginForm.startAnimation(slideOutLeft)
            layoutLoginForm.gone()

            layoutRegForm.startAnimation(slideInRight)
            layoutRegForm.visible()

            clearAllLoginFields()
        } else {
            layoutRegForm.startAnimation(slideOutRight)
            layoutRegForm.gone()

            layoutLoginForm.startAnimation(slideInLeft)
            layoutLoginForm.visible()

            clearAllRegFields()
        }
        toggleProgressBarVisibility(false)

        updateLoginErrorMessage(null)
        updateRegErrorMessage(null)
    }

    private fun onCreateUserFinished(isFinished: Boolean?) {
        if (isFinished == true) {
            updateLoginErrorMessage(null)
            hideKeyboard()
            clearAllRegFields()

            Toast.makeText(
                requireContext(),
                R.string.login_create_account_success,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun clearAllRegFields() {
        tfRegName.text?.clear()
        tfRegEmail.text?.clear()
        tfRegPassword.text?.clear()
    }

    private fun clearAllLoginFields() {
        tfLoginEmail.text?.clear()
        tfLoginPass.text?.clear()
    }

    private fun handleAuthenticationState(authenticationState: AuthenticationState?) {
        toggleProgressBarVisibility(false)

        when (authenticationState) {
            AuthenticationState.UNAUTHENTICATED -> {
                updateLoginErrorMessage(null)
                layoutLoginForm.visible()
                placeholderLayout.gone()
            }

            AuthenticationState.INVALID_AUTHENTICATION -> {
                //updateLoginErrorMessage(resources.getString(R.string.login_invalid_email_or_pass))
            }

            AuthenticationState.AUTHENTICATED -> {
                layoutLoginForm.gone()
                placeholderLayout.visible()

                findNavController().navigate(R.id.action_when_login_success)
            }

            else -> {}
        }
    }

    private fun updateLoginErrorMessage(message: String?) {
        if (message == null) {
            tvLoginErrorMessage.gone()

        } else {
            tvLoginErrorMessage.visible()
            tvLoginErrorMessage.text = message
        }
    }

    private fun updateRegErrorMessage(message: String?) {
        if (message == null) {
            tvRegErrorMessage.gone()
        } else {
            tvRegErrorMessage.visible()
            tvRegErrorMessage.text = message
        }
    }

    private fun handleFailure(failure: Failure?) {
        if (failure != null) {
            parent?.hideLoadingProgress()

            when (failure) {
                Failure.ServerError -> showFailureDialog(
                    title = "Server Failure",
                    message = "Sorry, server encountered an error",
                    onDismissCallback = {
                        close()
                    })
                Failure.NetworkConnection -> showFailureDialog(
                    title = "Network Connection Failure",
                    message = "Failed to connect to network. Please try again later.",
                    onDismissCallback = {
                        close()
                    })
                is Failure.FeatureFailure -> handleUseCaseFailure(failure as AuthenticateUseCaseFailure)
            }
            authenticateViewModel.failure.value = null
        }
    }

    private fun handleUseCaseFailure(failure: AuthenticateUseCaseFailure?) {
        when (failure) {
            is FailedToRegisterUser -> {
                updateRegErrorMessage(failure.message)
            }
            is FailedToLoginUser -> {
                updateLoginErrorMessage(failure.customErrorMessage.ifEmpty { failure.message })
            }
            else -> {
                failure?.let {
                    updateRegErrorMessage(failure.message)
                }
            }
        }
    }

    companion object {

        private val TAG = LoginFragment::class.java.simpleName

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        @JvmStatic
        fun newInstance() = LoginFragment()
    }

}