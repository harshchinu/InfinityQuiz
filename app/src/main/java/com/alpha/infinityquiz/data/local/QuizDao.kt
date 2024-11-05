package com.alpha.infinityquiz.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alpha.infinityquiz.data.model.QuestionEntity

@Dao
interface QuizDoa {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(question: QuestionEntity)

    @Query("SELECT * from questions")
    suspend fun getAllQuestions():List<QuestionEntity>

    @Delete
    suspend fun deleteBookmark(question: QuestionEntity)

    @Query("SELECT * FROM questions WHERE bookmarked = 1")
    suspend fun getBookmarkedQuestions(): List<QuestionEntity>

    @Query("SELECT id FROM questions")
    suspend fun getAllQuestionIds(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM questions WHERE id = :questionId LIMIT 1)")
    suspend fun isQuestionBookmarked(questionId: String): Boolean

}