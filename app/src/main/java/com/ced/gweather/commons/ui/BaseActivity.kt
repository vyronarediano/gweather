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

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.ced.commons.ui.extensions.displayKeyboard
import com.ced.commons.ui.extensions.inTransaction
import com.ced.gweather.R

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 * @since 1.0.0
 */
abstract class BaseActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout())

        toolbar = findViewById(toolbar())
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            actionBar = supportActionBar ?: return
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }

        addFragment(savedInstanceState)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            R.id.frameContent
        ) as BaseFragment).onBackPressed()
    }

    fun close() {
        super.onBackPressed()
        currentFocus?.displayKeyboard = false
    }

    fun setTitle(title: String) {
        actionBar.title = title
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction {
            add(R.id.frameContent, fragment())
        }


    abstract fun fragment(): BaseFragment
    abstract fun layout(): Int
    abstract fun toolbar(): Int
}

/**
 * Method creates fragment transaction and replace current fragment with new one.
 *
 * @param newFragment    new fragment used for replacement.
 * @param transactionTag text identifying fragment transaction.
 */
fun FragmentActivity?.replaceFragment(newFragment: Fragment, tag: String?) {
    val manager = this?.supportFragmentManager
    val tagFragment: Fragment? = manager?.findFragmentByTag(tag)
    if (tagFragment != null) {
        manager.popBackStackImmediate(tag, 0)
    } else {
        val transaction: FragmentTransaction? = manager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.frameContent, newFragment, tag)
        transaction?.addToBackStack(tag)
        transaction?.commit()
    }
}

