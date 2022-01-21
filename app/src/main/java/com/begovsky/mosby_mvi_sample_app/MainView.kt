package com.begovsky.mosby_mvi_sample_app

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView : MvpView {

    fun askQuestionIntent(): Observable<Int>

    fun getAnswerIntent(): Observable<Int>

    fun render(viewState: MainViewState)
}