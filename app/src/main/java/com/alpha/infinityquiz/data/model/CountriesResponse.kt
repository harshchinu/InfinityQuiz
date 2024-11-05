package com.alpha.infinityquiz.data.model

// CountriesResponse.kt
data class CountriesResponse(
    val data: Map<String, CountryInfo>
)

data class CountryInfo(
    val country: String
)

data class Country(
    val code: String,
    val name: String
)

fun CountriesResponse.toCountryList(): List<Country> {
    return data.map { (code, info) ->
        Country(code = code, name = info.country)
    }.sortedBy { it.name }
}
