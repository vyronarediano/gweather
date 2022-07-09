package com.ced.gweather.auth

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ced.commons.util.log.Logger
import com.ced.gweather.R
import com.google.android.material.snackbar.Snackbar

class AuthenticateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Logger.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Logger.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                }
                else -> {
                    Snackbar.make(findViewById(android.R.id.content), "Permission was denied.", Snackbar.LENGTH_LONG)
                        .setAction("GWEATHER APP SETTING") {
                            showIntentAppSettingsScreen()
                        }
                        .setActionTextColor(
                            ContextCompat.getColor(
                                this,
                                R.color.colorPrimary
                            )
                        )
                        .show()
                }
            }
        }
    }

    private fun showIntentAppSettingsScreen() {
        // Build intent that displays the App settings screen.
        val i = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + "com.ced.gweather")
        )
        startActivity(i)
    }

    companion object {

        private val TAG = AuthenticateActivity::class.java.simpleName

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

}