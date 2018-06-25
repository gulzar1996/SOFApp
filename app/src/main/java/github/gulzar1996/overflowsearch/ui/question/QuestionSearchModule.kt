package github.gulzar1996.overflowsearch.ui.question

import dagger.Module
import dagger.Provides
import github.gulzar1996.overflowsearch.data.local.question.IQuestionSearchRepository
import github.gulzar1996.overflowsearch.data.local.question.QuestionSearchRepository

@Module
class QuestionSearchModule {

    @Provides
    fun provideHomeInteractor(repo: QuestionSearchRepository)
            : IQuestionSearchRepository = repo


}