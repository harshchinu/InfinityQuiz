package com.alpha.infinityquiz.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val id: String,
    val question: String,
    val questionTYPE: SUPPORTED_CONTENT_TYPE,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int,
    val bookmarked: Boolean = false,
    val solution: List<Solution>,
    val sort: Int // This will hold the sorting order
)


fun Question.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = uuidIdentifier,
        question = question,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        correctOption = correctOption,
        questionTYPE = questionType,
        bookmarked = false, // Default or fetch from a source if needed
        sort = sort, // Use the sort field from `Question` for ordering
        solution = solution
    )
}


