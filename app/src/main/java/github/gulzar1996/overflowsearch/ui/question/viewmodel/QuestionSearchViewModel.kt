package github.gulzar1996.overflowsearch.ui.question.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import github.gulzar1996.overflowsearch.data.local.question.IQuestionSearchRepository

class QuestionSearchViewModel(val repository: IQuestionSearchRepository) : ViewModel() {
    private val questionQuery = MutableLiveData<String>()
    private val repoResult = Transformations.map(questionQuery, {
        repository.getQuestionsByQuery(it, 10)
    })
    val questions = Transformations.switchMap(repoResult, { it.pagedList })!!
    val networkState = Transformations.switchMap(repoResult, { it.networkState })!!
    val refreshState = Transformations.switchMap(repoResult, { it.refreshState })!!

    val navigateTrigger = MutableLiveData<String>()

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun itemClick(questionId: String) {
        navigateTrigger.value = questionId
        Log.d("SearchViewModel", "question Id clicked $questionId")
    }

    fun showQuestions(questionQuery: String): Boolean {
        if (this.questionQuery.value == questionQuery) {
            return false
        }
        this.questionQuery.value = questionQuery
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun currentQuery(): String? = questionQuery.value
}