/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ced.gweather.commons.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import com.ced.commons.ui.extensions.displayKeyboard
import com.ced.commons.ui.extensions.gone
import com.ced.commons.ui.extensions.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 * @since 1.0.0
 */
abstract class BaseFragment : Fragment() {

    abstract fun layoutId(): Int

    var toolbarTitle: String? = null
    var homeNavigationIcon: Int? = null
    var parent: BaseFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(layoutId(), container, false)

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showSnackBar(@StringRes message: Int) =
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    internal fun showSnackBar(message: String) =
        Snackbar.make(viewContainer, message, Snackbar.LENGTH_SHORT).show()

    open fun onBackPressed() {
        close()
    }

    open fun showLoadingProgress() {
        view?.findViewWithTag<ContentLoadingProgressBar>("progressBar")
            ?.apply {
                this.visible()
                this.progress = 50
            }
    }

    open fun hideLoadingProgress() {
        view?.findViewWithTag<ContentLoadingProgressBar>("progressBar")
            ?.apply {
                this.gone()
                this.progress = 100
            }
    }

    fun close() {
        hideKeyboard()
        (activity as BaseActivity).close()
    }

    fun hideKeyboard() {
        requireActivity().currentFocus?.displayKeyboard = false
    }

    fun showKeyboard() {
        requireActivity().currentFocus?.displayKeyboard = true
    }

    fun setActionBarTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    fun showFailureDialog(
        title: String = "Application Failure",
        message: String = "Sorry, we encountered an error performing the action.",
        onDismissCallback: (() -> Unit)? = null
    ) =

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton("Close", null)
            .setOnDismissListener {
                onDismissCallback.let {
                    if (it != null) {
                        it()
                    }
                }
            }
            .create()
            .show()

}

val BaseFragment.viewContainer: View get() = (activity as BaseActivity).frameContent

val BaseFragment.appContext: Context get() = activity?.applicationContext!!


