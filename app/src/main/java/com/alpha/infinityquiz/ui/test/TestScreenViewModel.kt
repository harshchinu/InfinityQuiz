package com.alpha.infinityquiz.ui.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.infinityquiz.data.model.toDomain
import com.alpha.infinityquiz.data.model.toEntity
import com.alpha.infinityquiz.data.usecase.CheckQuestionBookmarkedUseCase
import com.alpha.infinityquiz.data.usecase.FetchAndStoreQuestionsUseCase
import com.alpha.infinityquiz.data.usecase.GetBookmarkedQuestionsUseCase
import com.alpha.infinityquiz.data.usecase.ToggleBookmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestScreenViewModel @Inject constructor(
    private val fetchQuestionsUseCase: FetchAndStoreQuestionsUseCase,
    private val getBookmarkedQuestionsUseCase: GetBookmarkedQuestionsUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase,
    private val checkQuestionBookmarkedUseCase: CheckQuestionBookmarkedUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isBookmarkFlow = savedStateHandle.get<Boolean>("bookmarked") ?: false
    private val _state = MutableStateFlow(TestScreenState())
    val state: StateFlow<TestScreenState> = _state

    private val _timerState = MutableStateFlow(30) // Separate timer state
    val timerState: StateFlow<Int> = _timerState


    fun startNewQuestionTimer() {
        _timerState.value = 30 // Reset to 30 seconds for each new question
        startCountdown()
    }

    private var countdownJob: Job? = null

    private var currentIndex = 0 // Keeps track of the current question index

    init {
        initializeQuestions()
    }

    fun resetScore(){

    }



    fun startCountdown() {
        countdownJob?.cancel() // Cancel any existing countdown
        countdownJob = viewModelScope.launch {
            while (_timerState.value > 0) {
                delay(1000L)
                _timerState.value -= 1
            }
            goToNextQuestion() // Move to the next question when timer reaches 0
        }
    }

    fun checkAnswer(selectedOption: Int) {
        val question = _state.value.currentQuestion ?: return

        // Check if the question has already been answered
        if (_state.value.hasAnswered) {
            return // Prevent updating the score multiple times
        }

        // Check if the answer is correct
        val isCorrect = question.correctOption == selectedOption
        _state.value = _state.value.copy(
            isAnswerCorrect = isCorrect,
            score = if (isCorrect) _state.value.score + 1 else _state.value.score,
            hasAnswered = true // Mark the question as answered
        )

        // Optionally, go to the next question after a delay
        viewModelScope.launch {
            delay(4000L) // Delay to show feedback for 1 second
            goToNextQuestion()
        }
    }

    private fun initializeQuestions() {
        viewModelScope.launch {
            if (isBookmarkFlow) {
                loadBookmarkedQuestions()
            } else {
                fetchQuestionsUseCase() // Fetch questions from API if not already in DB
                loadAllQuestions()
            }
        }
    }

    // Load all questions for the normal test flow
    private fun loadAllQuestions() {
        viewModelScope.launch {
            val questions = fetchQuestionsUseCase.invoke() // Assume this function returns all questions as a list
            val sorted = questions.sortedBy { it.sort }
            _state.value = _state.value.copy(
                questions = sorted.map { it.toDomain() },
                currentQuestion = sorted.getOrNull(currentIndex)?.toDomain(),
                isBookmarked = sorted.getOrNull(currentIndex)?.bookmarked?:false
            )
        }
        startNewQuestionTimer()
    }

    // Load only bookmarked questions for the bookmarked flow
    private fun loadBookmarkedQuestions() {
        viewModelScope.launch {
            getBookmarkedQuestionsUseCase().let {   bookmarkedQuestions ->
                val sorted = bookmarkedQuestions.sortedBy { it.sort }
                _state.value = _state.value.copy(
                    questions = sorted,
                    currentQuestion = sorted.getOrNull(currentIndex),
                    isBookmarked = true
                )
            }
        }
        startNewQuestionTimer()
    }

    fun goToNextQuestion() {
        viewModelScope.launch {
            val showScoreBottomSheet = (currentIndex + 1)% 5 == 0
            val questions = _state.value.questions
            if (currentIndex < questions.size - 1) {
                currentIndex++
                _state.value = _state.value.copy(
                    currentQuestion = questions[currentIndex],
                    isBookmarked = checkQuestionBookmarkedUseCase.invoke(questions[currentIndex].uuidIdentifier),
                    displayScoreBottomSheet = showScoreBottomSheet,
                    hasAnswered = false, // Reset hasAnswered for the new question
                    isAnswerCorrect = null
                )
            }
        }
        startNewQuestionTimer()
    }

    // Toggle the bookmark status of the current question
    fun toggleBookmarkCurrentQuestion() {
        val question = _state.value.currentQuestion ?: return
        viewModelScope.launch {
            val isBookmarked = _state.value.isBookmarked
            toggleBookmarkUseCase(question.toEntity(), !isBookmarked)
            _state.value = _state.value.copy(isBookmarked = !isBookmarked)
        }
    }
}
