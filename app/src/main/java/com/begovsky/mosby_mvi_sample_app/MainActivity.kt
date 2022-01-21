package com.begovsky.mosby_mvi_sample_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import java.util.*

class MainActivity : MviActivity<MainView, MainPresenter>(),MainView {

    private lateinit var textView: TextView
    private lateinit var askQuestionBtn: Button
    private lateinit var getAnswerBtn: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view)
        askQuestionBtn = findViewById(R.id.btn_question)
        getAnswerBtn = findViewById(R.id.btn_answer)
        progressBar = findViewById(R.id.progress_bar)
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun askQuestionIntent(): Observable<Int> {
        return askQuestionBtn.clicks()
            .map { click -> Random().nextInt() }
    }

    override fun getAnswerIntent(): Observable<Int> {
        return getAnswerBtn.clicks()
            .map { click -> Random().nextInt() }
    }

    override fun render(viewState: MainViewState) {
        when(viewState.partial){
            is PartialMainState.Loading -> {
                progressBar.visibility = View.VISIBLE
                askQuestionBtn.isEnabled = false
                getAnswerBtn.isEnabled = false
            }
            is PartialMainState.GotAnswer -> {
                progressBar.visibility = View.GONE
                askQuestionBtn.isEnabled = true
                getAnswerBtn.isEnabled = false
                textView.text = viewState.textToShow
            }
            is PartialMainState.GotQuestion -> {
                progressBar.visibility = View.GONE
                askQuestionBtn.isEnabled = false
                getAnswerBtn.isEnabled = true
                textView.text = viewState.textToShow
            }
            is PartialMainState.Error -> {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
            else -> {
                progressBar.visibility = View.GONE
                askQuestionBtn.isEnabled = true
                getAnswerBtn.isEnabled = true
            }

        }
    }
}