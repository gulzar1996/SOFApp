package github.gulzar1996.overflowsearch.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import github.gulzar1996.overflowsearch.ui.question.QuestionSearchActivity
import github.gulzar1996.overflowsearch.ui.question.QuestionSearchModule


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [(QuestionSearchModule::class)])
    abstract fun bindQuestionSearchActivity(): QuestionSearchActivity

}