package github.gulzar1996.overflowsearch.data.local.answer

import android.arch.lifecycle.LiveData
import github.gulzar1996.overflowsearch.data.model.answer.Answer
import github.gulzar1996.overflowsearch.data.model.search.Listing
import github.gulzar1996.overflowsearch.data.model.search.Question

interface IAnswerRepository {
    fun getAnswers(questionId: String, pageSize: Int): Listing<Answer>
    fun getQuestion(questionId: String): LiveData<Question>
}