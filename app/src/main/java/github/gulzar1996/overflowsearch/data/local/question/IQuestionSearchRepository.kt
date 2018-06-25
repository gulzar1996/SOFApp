package github.gulzar1996.overflowsearch.data.local.question

import github.gulzar1996.overflowsearch.data.model.search.Listing
import github.gulzar1996.overflowsearch.data.model.search.Question

interface IQuestionSearchRepository {

    fun getQuestionsByQuery(questionQuery: String, pageSize: Int): Listing<Question>

}