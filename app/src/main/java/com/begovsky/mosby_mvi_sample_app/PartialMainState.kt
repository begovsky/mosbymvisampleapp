package com.begovsky.mosby_mvi_sample_app

sealed class PartialMainState {
    object Loading : PartialMainState()
    data class GotQuestion(var question: String) : PartialMainState()
    data class GotAnswer(var answer: String) : PartialMainState()
    data class Error(var error: Throwable) : PartialMainState()
}