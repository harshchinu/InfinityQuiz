package com.alpha.infinityquiz.data.remote
import com.alpha.infinityquiz.data.model.CountriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface CountriesApiService {

    @GET("data/v1/countries")
    suspend fun getCountries(): Response<CountriesResponse>
}
