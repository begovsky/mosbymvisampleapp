package com.begovsky.mosby_mvi_sample_app

data class MainViewState(
    val partial: PartialMainState? =null,
    var loading: Boolean = false,
    var questionShown: Boolean = false,
    var answerShown: Boolean = false,
    var textToShow: String = "Ask Your Question",
    var error: Throwable? = null
)