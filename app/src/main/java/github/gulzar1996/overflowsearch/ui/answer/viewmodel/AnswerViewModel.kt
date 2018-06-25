package github.gulzar1996.overflowsearch.ui.answer.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import github.gulzar1996.overflowsearch.data.local.answer.IAnswerRepository

class AnswerViewModel(val repository: IAnswerRepository) : ViewModel() {
    private val questionId = MutableLiveData<String>()

    val questionResult = Transformations.map(questionId, {
        repository.getQuestion(it)
    })

    private val repoResult = Transformations.map(questionId, {
        repository.getAnswers(it, 10)
    })
    val answers = Transformations.switchMap(repoResult, { it.pagedList })!!
    val networkState = Transformations.switchMap(repoResult, { it.networkState })!!
    val refreshState = Transformations.switchMap(repoResult, { it.refreshState })!!
    val question = Transformations.switchMap(questionResult, { it })!!


    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun showAnswers(questionId: String): Boolean {
        if (this.questionId.value == questionId) {
            return false
        }
        this.questionId.value = questionId
        return true
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }

    fun showQuestion(questionId: String) {
        this.questionId.value = questionId
    }

    fun currentQuestionId(): String? = questionId.value
}