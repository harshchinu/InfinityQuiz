package com.alpha.infinityquiz.util

import android.content.Context
import android.media.SoundPool
import com.alpha.infinityquiz.R

class SoundManager(private val context: Context) {
    private val soundPool = SoundPool.Builder().setMaxStreams(1).build()
    private var correctSound: Int = 0

    fun loadSounds() {
      // correctSound = soundPool.load(context, R.raw.correct_answer, 1)
    }

    fun playCorrectSound() {
        soundPool.play(correctSound, 1F, 1F, 1, 0, 1F)
    }
}
