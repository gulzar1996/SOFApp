package github.gulzar1996.overflowsearch.ui.question

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import github.gulzar1996.overflowsearch.data.local.question.QuestionSearchRepository
import github.gulzar1996.overflowsearch.ui.question.viewmodel.QuestionSearchViewModel
import github.gulzar1996.overflowsearch.ui.question.viewmodel.QuestionSearchViewModelFactory

class QuestionSearchActivity : AppCompatActivity() {

    lateinit var viewModel: QuestionSearchViewModel


    lateinit var questionSearchRepository: QuestionSearchRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = getVM()

    }

    private fun getVM() =
            ViewModelProviders.of(this, QuestionSearchViewModelFactory(questionSearchRepository))
                    .get(QuestionSearchViewModel::class.java)


}