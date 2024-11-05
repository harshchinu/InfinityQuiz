package com.alpha.infinityquiz.di

import android.content.Context
import androidx.room.Room
import com.alpha.infinityquiz.data.local.QuizDatabase
import com.alpha.infinityquiz.data.local.QuizDoa
import com.alpha.infinityquiz.data.remote.ApiService
import com.alpha.infinityquiz.data.remote.CountriesApiService
import com.alpha.infinityquiz.data.repository.QuestionRepository
import com.alpha.infinityquiz.data.usecase.CheckQuestionBookmarkedUseCase
import com.alpha.infinityquiz.data.usecase.FetchAndStoreQuestionsUseCase
import com.alpha.infinityquiz.data.usecase.GetBookmarkedQuestionsUseCase
import com.alpha.infinityquiz.data.usecase.ToggleBookmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val COUNTRIES_BASE_URL = "https://api.first.org/"
    private const val QUESTIONS_BASE_URL = "https://run.mocky.io/"

    @CountriesRetrofit
    @Provides
    @Singleton
    fun provideCountriesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(COUNTRIES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @QuestionsRetrofit
    @Provides
    @Singleton
    fun provideQuestionsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(QUESTIONS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCountriesApiService(@CountriesRetrofit retrofit: Retrofit): CountriesApiService {
        return retrofit.create(CountriesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiService(@QuestionsRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): QuizDatabase {
        return Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            "quiz_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: QuizDatabase): QuizDoa {
        return database.quizDoa()
    }

    @Provides
    @Singleton
    fun provideQuizRepository(quizDoa: QuizDoa, apiService: ApiService): QuestionRepository{
        return QuestionRepository(apiService,quizDoa)
    }

    @Provides
    @Singleton
    fun provideFetchStoreUseCase(repository: QuestionRepository):FetchAndStoreQuestionsUseCase = FetchAndStoreQuestionsUseCase(repository)

    @Provides
    @Singleton
    fun provideToggleBookmarkUsecase(repository: QuestionRepository):ToggleBookmarkUseCase = ToggleBookmarkUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllBookmarkQuestion(repository: QuestionRepository):GetBookmarkedQuestionsUseCase = GetBookmarkedQuestionsUseCase(repository)

    @Provides
    @Singleton
    fun provideCheckIfBookmarkerdUseCase(repository: QuestionRepository):CheckQuestionBookmarkedUseCase = CheckQuestionBookmarkedUseCase(repository)

}
