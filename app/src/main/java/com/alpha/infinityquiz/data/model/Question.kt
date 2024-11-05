package com.alpha.infinityquiz.data.model

data class Question(
    val uuidIdentifier: String,
    val questionType: SUPPORTED_CONTENT_TYPE,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val correctOption: Int,
    val sort:Int,
    val solution: List<Solution>
)

data class Solution(
    val contentType: SUPPORTED_CONTENT_TYPE,
    val contentData: String
)

enum class SUPPORTED_CONTENT_TYPE{
    text,htmlText,image
}

fun QuestionEntity.toDomain(): Question {
    return Question(
        uuidIdentifier = id,
        questionType = questionTYPE, // Set default or fetch if needed
        question = question,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        option4 = option4,
        correctOption = correctOption,
        sort = sort, // Preserve sort for ordered retrieval
        solution = solution,// Use default or populate if data is available
    )
}
