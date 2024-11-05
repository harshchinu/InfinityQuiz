package com.alpha.infinityquiz.ui.test

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.alpha.infinityquiz.data.model.Question
import com.alpha.infinityquiz.data.model.SUPPORTED_CONTENT_TYPE
import com.alpha.infinityquiz.data.model.Solution

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TestScreen(
    viewModel: TestScreenViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val timer by viewModel.timerState.collectAsState()

    val lazyListState = rememberLazyListState()
    LaunchedEffect(state.currentQuestion) {
        lazyListState.scrollToItem(0)
    }

    LaunchedEffect(state.displayScoreBottomSheet) {
        if (state.displayScoreBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (state.displayScoreBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.resetScore() },
            sheetState = sheetState
        ) {
            ScoreBottomSheet(
                score = state.score,
                onContinue = { viewModel.resetScore() }
            )
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        stickyHeader {
            TopBar(timer = timer)
        }

        state.currentQuestion?.let { currentQuestion ->
            // Question Content
            item {
                QuestionContent(currentQuestion)
            }

            // Options List
            itemsIndexed(
                listOf(
                    currentQuestion.option1,
                    currentQuestion.option2,
                    currentQuestion.option3,
                    currentQuestion.option4
                )
            ) { index, option ->
                OptionsList(option = option, onClick = { viewModel.checkAnswer(index + 1) })
            }

            // Bottom Row for Bookmark, Next Question, and Feedback
            item {
                BottomRow(
                    isBookmarked = state.isBookmarked,
                    isAnswerCorrect = state.isAnswerCorrect,
                    onToggleBookmark = { viewModel.toggleBookmarkCurrentQuestion() },
                    onNextQuestion = { viewModel.goToNextQuestion() }
                )
            }

            // Solution Content
            state.isAnswerCorrect?.let {
                items(currentQuestion.solution) { solution ->
                    SolutionContent(solution = solution)
                }
            }

            // Display score
            item {
                ScoreDisplay(score = state.score)
            }
        } ?: item {
            // Finish Button
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        }
    }

}

@Composable
fun QuestionContent(currentQuestion: Question) {
    when (currentQuestion.questionType) {
        SUPPORTED_CONTENT_TYPE.text -> {
            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        SUPPORTED_CONTENT_TYPE.htmlText -> {
            Text(
                text = AnnotatedString.fromHtml(currentQuestion.question),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        SUPPORTED_CONTENT_TYPE.image -> {
            Image(
                painter = rememberImagePainter(data = currentQuestion.question),
                contentDescription = null,
                modifier = Modifier.size(500.dp, 400.dp)
            )
        }
    }
}

@Composable
fun OptionsList(option: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(text = option)
    }
}

@Composable
fun BottomRow(
    isBookmarked: Boolean,
    isAnswerCorrect: Boolean?,
    onToggleBookmark: () -> Unit,
    onNextQuestion: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isBookmarked) "Remove Bookmark" else "Add Bookmark"
            )
        }

        Button(
            onClick = onNextQuestion,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Next Question")
        }

        isAnswerCorrect?.let { isCorrect ->
            Text(
                text = if (isCorrect) "Correct!" else "Incorrect!",
                color = if (isCorrect) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SolutionContent(solution: Solution) {
    when (solution.contentType) {
        SUPPORTED_CONTENT_TYPE.text -> Text(text = solution.contentData)
        SUPPORTED_CONTENT_TYPE.htmlText -> Text(text = AnnotatedString.fromHtml(solution.contentData))
        SUPPORTED_CONTENT_TYPE.image -> Image(
            painter = rememberImagePainter(data = solution.contentData),
            contentDescription = null,
            modifier = Modifier.size(500.dp, 400.dp)
        )
    }
}

@Composable
fun ScoreDisplay(score: Int) {
    Text(text = "Score: $score")
}

@Composable
fun TopBar(timer: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Time Left: $timer seconds",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}