package com.alpha.infinityquiz.data.repository

import com.alpha.infinityquiz.data.local.QuizDoa
import com.alpha.infinityquiz.data.model.Question
import com.alpha.infinityquiz.data.model.QuestionEntity
import com.alpha.infinityquiz.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val apiService: ApiService,
    private val quizDoa: QuizDoa
) {
    // Fetch questions from the API
    suspend fun fetchQuestionsFromApi(): Response<List<Question>> {
        return apiService.getQuestions()
    }

    suspend fun getAllQuestions(): List<QuestionEntity> = quizDoa.getAllQuestions()

    suspend fun getAllBookmarkedQuestions(): List<QuestionEntity> = quizDoa.getBookmarkedQuestions()

    // Store questions locally
    suspend fun storeQuestions(questions: List<QuestionEntity>) {
        quizDoa.insertAllQuestions(questions)
    }

    suspend fun getAllQuestionIds(): List<String> {
        return quizDoa.getAllQuestionIds()
    }


    // Add or remove bookmarks
    suspend fun addBookmark(question: QuestionEntity) {
        quizDoa.insertBookmark(question)
    }

    suspend fun removeBookmark(question: QuestionEntity) {
        quizDoa.deleteBookmark(question)
    }

    suspend fun checkIfBookmarked(id:String):Boolean{
        return quizDoa.isQuestionBookmarked(id)
    }
}
