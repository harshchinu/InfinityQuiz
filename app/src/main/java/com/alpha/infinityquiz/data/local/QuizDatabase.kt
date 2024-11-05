package com.alpha.infinityquiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpha.infinityquiz.data.model.QuestionEntity


@Database(entities = [QuestionEntity::class], version = 1)
@TypeConverters(SolutionTypeConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDoa(): QuizDoa
}

