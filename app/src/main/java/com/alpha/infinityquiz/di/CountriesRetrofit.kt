package com.alpha.infinityquiz.di;

import javax.inject.Qualifier

import kotlin.annotation.AnnotationRetention;

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CountriesRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QuestionsRetrofit
