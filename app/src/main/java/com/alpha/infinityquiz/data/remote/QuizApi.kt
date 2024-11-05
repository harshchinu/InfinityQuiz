package com.alpha.infinityquiz.data.remote

import com.alpha.infinityquiz.data.model.Question
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v3/c3d407ac-5b23-43f6-af4f-7d0e11c746b4")
    suspend fun getQuestions(): Response<List<Question>>
}
