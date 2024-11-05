package com.alpha.infinityquiz.data.usecase

import com.alpha.infinityquiz.data.model.Question
import com.alpha.infinityquiz.data.model.QuestionEntity
import com.alpha.infinityquiz.data.model.toDomain
import com.alpha.infinityquiz.data.model.toEntity
import com.alpha.infinityquiz.data.repository.QuestionRepository
import javax.inject.Inject

class FetchAndStoreQuestionsUseCase @Inject constructor(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke():List<QuestionEntity> {
        val response = repository.fetchQuestionsFromApi()
        if (response.isSuccessful) {
            response.body()?.let { questions ->
                val existingQuestionIds = repository.getAllQuestionIds()
                val newQuestions = questions.filter { it.uuidIdentifier !in existingQuestionIds }
                repository.storeQuestions(newQuestions.map { it.toEntity() })
            }
            return repository.getAllQuestions()
        }
        return emptyList()
    }
}
