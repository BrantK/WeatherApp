package com.brant.weatherapp.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brant.weatherapp.R
import com.brant.weatherapp.data.model.City
import com.brant.weatherapp.data.viewmodel.MainViewModel
import com.brant.weatherapp.ui.adapter.CityListAdapter
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_city_list.*

class CityListFragment : Fragment(R.layout.fragment_city_list) {

  private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ViewModelProvider(this)[MainViewModel::class.java]
  }

  private val cityListAdapter = CityListAdapter{ city ->
    //Set the city that was clicked as the selected city then go to the forecast screen
    viewModel.setSelectedCity(city)
  }

  private val startAutocomplete = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult(),
    (ActivityResultCallback { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        result.data?.let { resultIntent ->
          val place = Autocomplete.getPlaceFromIntent(resultIntent)

          //If the required fields are not null, add the city to the list
          if (place.address != null && place.latLng?.latitude != null && place.latLng?.longitude != null) {
            viewModel.getWeatherDataAndAddCity(
              City(
                place.address!!.substringBeforeLast(","),
                place.latLng!!.latitude,
                place.latLng!!.longitude,
              )
            )
          }
        }
      }
    })
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    city_recycler_view.adapter = cityListAdapter
    city_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    activity?.toolbar_title?.text = getString(R.string.title_cities)
    activity?.cities_button?.visibility = View.INVISIBLE
    activity?.add_city_button?.visibility = View.VISIBLE
    activity?.back_button?.visibility = if (!viewModel.selectedCityAvailable()) View.INVISIBLE else View.VISIBLE

    activity?.back_button?.setOnClickListener {
      activity?.onBackPressed()
    }
    activity?.add_city_button?.setOnClickListener {
      launchAutoCompleteIntent()
    }

    viewModel.citiesMap.observe(requireActivity()) {
      if (it.isNullOrEmpty()) {
        empty_list_text?.visibility = View.VISIBLE
        activity?.back_button?.visibility = View.INVISIBLE
      } else {
        empty_list_text?.visibility = View.GONE
      }
      cityListAdapter.updateList(it.values.toList())
    }

    attachTouchHelper()
  }

  private fun launchAutoCompleteIntent() {
    val fields = mutableListOf(
      Place.Field.ADDRESS,
      Place.Field.LAT_LNG,
    )
    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
      .setHint("Search for City")
      .setCountry("US")
      .setTypeFilter(TypeFilter.CITIES)
      .build(requireContext())
    startAutocomplete.launch(intent)
  }

  //Touch Helper for removing cities from the list by swipe
  private fun attachTouchHelper() {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        try {
          val cityList = viewModel.citiesMap.value?.values?.toList() ?: emptyList()
          val position = viewHolder.adapterPosition
          viewModel.removeCityFromList(cityList[position])
        } catch (e: Exception) {
          //It's possible we may try to get an item from the list that doesn't exist
          e.printStackTrace()
        }
      }
    }).attachToRecyclerView(city_recycler_view)
  }

  companion object {
    fun open(activity: AppCompatActivity) {
      activity.supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(
          R.anim.slide_in_from_right,
          R.anim.slide_out_to_right,
          R.anim.slide_in_from_left,
          R.anim.slide_out_to_right
        )
        .add(android.R.id.content, CityListFragment(), null)
        .addToBackStack(null)
        .commit()
    }
  }
}