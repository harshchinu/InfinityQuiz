package com.alpha.infinityquiz.data.local

import androidx.room.TypeConverter
import com.alpha.infinityquiz.data.model.Solution
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class SolutionTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromSolutionList(solutionList: List<Solution>?): String? {
        return gson.toJson(solutionList)
    }

    @TypeConverter
    fun toSolutionList(data: String?): List<Solution>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Solution>>() {}.type
        return gson.fromJson(data, listType)
    }
}