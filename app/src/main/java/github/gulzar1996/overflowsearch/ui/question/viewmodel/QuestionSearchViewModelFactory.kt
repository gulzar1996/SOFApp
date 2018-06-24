package github.gulzar1996.overflowsearch.ui.question.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import github.gulzar1996.overflowsearch.data.local.question.QuestionSearchRepository

class QuestionSearchViewModelFactory
(private val QuestionSearchRepository: QuestionSearchRepository) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T
            = QuestionSearchViewModel(QuestionSearchRepository) as T

}