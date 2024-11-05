package com.alpha.infinityquiz.data.usecase

import com.alpha.infinityquiz.data.model.QuestionEntity
import com.alpha.infinityquiz.data.model.toEntity
import com.alpha.infinityquiz.data.repository.QuestionRepository

class CheckQuestionBookmarkedUseCase(    private val repository: QuestionRepository
) {
    suspend operator fun invoke(id:String):Boolean {
        return repository.checkIfBookmarked(id)
    }
}
