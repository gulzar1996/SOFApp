package github.gulzar1996.overflowsearch.ui.answer

import dagger.Module
import dagger.Provides
import github.gulzar1996.overflowsearch.data.local.answer.AnswerRepository
import github.gulzar1996.overflowsearch.data.local.answer.IAnswerRepository

@Module
class AnswerModule {
    @Provides
    fun provideAnswerRepo(repo: AnswerRepository)
            : IAnswerRepository = repo

}