package com.alpha.infinityquiz.data.usecase

import com.alpha.infinityquiz.data.model.QuestionEntity
import com.alpha.infinityquiz.data.repository.QuestionRepository
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val repository: QuestionRepository
) {
    suspend operator fun invoke(question: QuestionEntity, shouldBookmark: Boolean) {
        val newQuestion = question.copy(bookmarked = shouldBookmark)
        repository.addBookmark(newQuestion)
    }
}
