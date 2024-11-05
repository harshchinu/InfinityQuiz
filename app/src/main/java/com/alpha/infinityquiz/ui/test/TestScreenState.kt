package com.alpha.infinityquiz.ui.test

import com.alpha.infinityquiz.data.model.Question

// TestScreenState.kt
data class TestScreenState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val currentQuestion: Question? = null,
    val isAnswerCorrect: Boolean? = null,
    val bookmarkedQuestions: List<Question> = emptyList(),
    val score: Int = 0,
    val isBookmarked: Boolean = false,
    val displayScoreBottomSheet:Boolean = false,
    val hasAnswered:Boolean = false
)
