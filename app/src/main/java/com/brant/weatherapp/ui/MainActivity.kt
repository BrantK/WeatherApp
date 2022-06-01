package com.brant.weatherapp.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.brant.weatherapp.R
import com.brant.weatherapp.data.repository.MainRepository
import com.brant.weatherapp.data.viewmodel.MainViewModel
import com.brant.weatherapp.utils.DialogManager
import com.google.android.libraries.places.api.Places
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ViewModelProvider(this)[MainViewModel::class.java]
  }

  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    getPlacesApiKey()?.let {
      Places.initialize(applicationContext, it)
    }

    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    navController = navHostFragment.navController
    nav_view.setupWithNavController(navController)

    //Handle loading and error states here
    viewModel.state.observe(this) {
      when (it) {
        MainRepository.State.LOADING -> {
          DialogManager.toggleProgressBar(this, true)
        }
        MainRepository.State.SUCCESS -> {
          DialogManager.toggleProgressBar(this, false)
        }
        MainRepository.State.ERROR -> {
          DialogManager.toggleProgressBar(this, false)
          DialogManager.showSnackbar(this, "An error occurred, please try again")
        }
        else -> {
          DialogManager.toggleProgressBar(this, false)
        }
      }
    }

    //If there was a city selected, popBackStack to come back to the home screen
    viewModel.selectedCity.observe(this) {
      if (it != null && supportFragmentManager.backStackEntryCount > 0) {
        supportFragmentManager.popBackStack()
      }
    }

    supportFragmentManager.addOnBackStackChangedListener {
      if (supportFragmentManager.backStackEntryCount == 0) {
        add_city_button.visibility = View.GONE
        back_button.visibility = View.INVISIBLE
        cities_button.visibility = View.VISIBLE
        if (navController.backQueue.size == 2) {
          toolbar_title.text = getString(R.string.title_current_forecast)
        }
      }
    }

    cities_button.setOnClickListener {
      CityListFragment.open(this)
    }

    swipe_refresh.setOnRefreshListener {
      viewModel.refreshWeatherData()
      swipe_refresh.isRefreshing = false
    }

    //If no city is currently selected, show the city list screen
    if (!viewModel.selectedCityAvailable()) {
      CityListFragment.open(this)
    }
  }

  override fun onBackPressed() {
    //If we're coming back from a different fragment, default to the 1st tab to avoid strange navigation behavior
    if (!viewModel.selectedCityAvailable()) {
      finish()
    } else {
      if (supportFragmentManager.backStackEntryCount > 0 && navController.backQueue.size > 2) {
        navController.popBackStack()
      }
      super.onBackPressed()
    }
  }

  override fun onStart() {
    super.onStart()
    viewModel.refreshWeatherData()
  }

  private fun getPlacesApiKey(): String? {
    return try {
      val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
      appInfo.metaData.getString("com.google.android.geo.API_KEY")
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }
}