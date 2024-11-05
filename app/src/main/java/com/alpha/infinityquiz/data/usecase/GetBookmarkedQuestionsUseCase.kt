package com.alpha.infinityquiz.data.usecase

import com.alpha.infinityquiz.data.model.Question
import com.alpha.infinityquiz.data.model.QuestionEntity
import com.alpha.infinityquiz.data.model.toDomain
import com.alpha.infinityquiz.data.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedQuestionsUseCase @Inject constructor(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(): List<Question> {
        return repository.getAllBookmarkedQuestions().map { it.toDomain() }
    }
}
