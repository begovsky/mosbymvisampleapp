package com.begovsky.mosby_mvi_sample_app


import com.begovsky.mosby_mvi_sample_app.PartialMainState.*
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers;


class MainPresenter : MviBasePresenter<MainView, MainViewState>() {

    private val dataSource: DataSource = DataSource()
    private var currentState = MainViewState()


    override fun bindIntents() {

        val askQuestion: Observable<PartialMainState> =
            intent(MainView::askQuestionIntent).switchMap { questionId ->
                dataSource.askQuestion(questionId).map { question ->
                    GotQuestion(question) as PartialMainState
                }.startWith(Loading)
                    .onErrorReturn { err ->
                        Error(err)
                    }.subscribeOn(Schedulers.io())
            }

        val getAnswer: Observable<PartialMainState> =
            intent(MainView::getAnswerIntent).switchMap { answerId ->
                dataSource.getAnswer(answerId).map { answer ->
                    GotAnswer(answer) as PartialMainState
                }.startWith(Loading)
                    .onErrorReturn { err ->
                        Error(err)
                    }.subscribeOn(Schedulers.io())
            }

        val allIntents: Observable<PartialMainState> = Observable.merge(
            askQuestion, getAnswer
        ).observeOn(AndroidSchedulers.mainThread())

        subscribeViewState(
            allIntents
                .observeOn(AndroidSchedulers.mainThread())
                .scan(currentState, this::viewStateReducer),
            MainView::render
        )
    }

    private fun viewStateReducer(
        previousState: MainViewState,
        changedStatePart: PartialMainState?
    ): MainViewState {
        currentState = when (changedStatePart) {
            is Loading -> {
                previousState.copy(loading = true)
            }
            is GotQuestion -> {
                previousState.copy(
                    loading = false,
                    questionShown = true,
                    answerShown = false,
                    textToShow = changedStatePart.question
                )
            }
            is GotAnswer -> {
                previousState.copy(
                    loading = false,
                    questionShown = false,
                    answerShown = true,
                    textToShow = changedStatePart.answer
                )
            }
            is Error -> {
                previousState.copy(loading = false, error = changedStatePart.error)
            }
            else -> {
                previousState.copy()
            }
        }

        currentState = currentState.copy(partial = changedStatePart)

        return currentState
    }

}