package com.alpha.infinityquiz.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.infinityquiz.data.model.Country
import com.alpha.infinityquiz.data.model.Question
import com.alpha.infinityquiz.data.model.toCountryList
import com.alpha.infinityquiz.data.remote.CountriesApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val apiService: CountriesApiService):ViewModel() {
    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        try {
        viewModelScope.launch {
            val response = apiService.getCountries()
            if (response.isSuccessful) {
                response.body()?.let {
                    _countries.value = it.toCountryList()
                }
            }
        }
        }catch (e:Exception){
            Log.e(TAG, "fetchCountries: ",e )
        }
    }

    fun setSelectedCountry(country: Country) {
        _selectedCountry.value = country
    }

    companion object{
        private const val TAG = "HomeScreenViewModel"
    }

}