package github.gulzar1996.overflowsearch.data.local.question

import github.gulzar1996.overflowsearch.data.Listing
import github.gulzar1996.overflowsearch.data.Question

interface IQuestionSearchRepository {

    fun getQuestionsByQuery(questionQuery: String, pageSize: Int): Listing<Question>

}