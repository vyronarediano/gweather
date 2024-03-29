package com.ced.gweather.weather.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ced.authentication.domain.model.User
import com.ced.commons.ui.extensions.displayKeyboard
import com.ced.commons.ui.extensions.viewModel
import com.ced.commons.ui.observe
import com.ced.gweather.R
import com.ced.gweather.auth.AuthenticateActivity
import com.ced.gweather.commons.ui.BaseFragment
import com.ced.gweather.databinding.NavHeaderMainBinding
import com.ced.gweather.weather.features.drawer.NavHeaderViewModel
import com.ced.gweather.weather.ui.weatherhome.WeatherHomeFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class MainActivity : BaseActivityDI() {

    private lateinit var navHeaderViewModel: NavHeaderViewModel

    override fun fragment() = WeatherHomeFragment.newInstance()
    override fun layout(): Int = R.layout.activity_main
    override fun toolbar(): Int = R.id.toolbar

    private var toolbar: Toolbar? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private val fragmentManager: FragmentManager = supportFragmentManager

    //region Overriden Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)

        navHeaderViewModel = viewModel(viewModelFactory) {
            observe(user, ::bindLoggedInUser)
        }

        initToolbar()
        initDrawerMenu()

        initFragmentManager(fragmentManager)

        navHeaderViewModel.loadLoggedInUser()
    }

    //endregion Overriden Methods

    //region Private Methods

    private fun initDrawerMenu() {
        toggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                drawerView.displayKeyboard = false
                super.onDrawerOpened(drawerView)
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView?.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true

            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    showLogoutConfirmation()
                }
                else -> {
                    displayFragment(menuItem.itemId, menuItem.title.toString())
                }
            }

            drawerLayout.closeDrawers()
            true
        }
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setBackgroundColor(ContextCompat.getColor(this, R.color.weather_current_bg))
        setSupportActionBar(toolbar)
    }

    private fun displayFragment(id: Int, title: String) {
        setTitle(title)

        initFragmentManager(fragmentManager)

        val fragment: Fragment? = when (id) {
            R.id.nav_home -> {
                // To determine the weather fetched will only be added to record/list when user opens the app only
                navHeaderViewModel.updateSessionIsAllowedToSave(false)

                WeatherHomeFragment.newInstance()
            }
            // future features
            else -> null
        }

        if (fragment != null) {
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameContent, fragment)
            fragmentTransaction.replace(R.id.frameContent, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun initFragmentManager(fragmentManager: FragmentManager?) {
        fragmentManager?.addOnBackStackChangedListener {
            if (fragmentManager.backStackEntryCount > 0) {
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar?.setDisplayHomeAsUpEnabled(true)

                val currentFragmentTag =
                    fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)
                        .name
                val currentFragment =
                    fragmentManager.findFragmentByTag(currentFragmentTag) as BaseFragment

                if (currentFragment.homeNavigationIcon != null) {
                    toolbar?.setNavigationIcon(currentFragment.homeNavigationIcon ?: -1)
                    toolbar?.title = currentFragment.toolbarTitle
                }

                toolbar?.setNavigationOnClickListener {
                    if (fragmentManager.backStackEntryCount > 0) {
                        onBackPressed()
                    }
                }
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                initDrawerMenu()
            }
        }
    }

    private fun bindLoggedInUser(user: User?) {
        try {
            val viewHeader = navigationView.getHeaderView(0)
            val binding = NavHeaderMainBinding.bind(viewHeader)
            binding.user = user
        } catch (e: Exception) {
            // do nothing
        }
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Exit Application")
            .setMessage(resources.getString(R.string.confirm_logout))
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { _, _ ->
                run {
                    finish()
                    startActivity(Intent(this, AuthenticateActivity::class.java))
                }
            }
            .create()
            .show()
    }

    //endregion Private Methods
}


