package github.gulzar1996.overflowsearch.ui.answer.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import github.gulzar1996.overflowsearch.data.local.answer.IAnswerRepository

class AnswerViewModelFactory
(private val answerRepository: IAnswerRepository) :
        ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = AnswerViewModel(answerRepository) as T

}