package github.gulzar1996.overflowsearch.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import github.gulzar1996.overflowsearch.OverFlowSearchApp
import github.gulzar1996.overflowsearch.di.module.AppModule
import github.gulzar1996.overflowsearch.di.builder.ActivityBuilder
import javax.inject.Singleton


@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: OverFlowSearchApp)

}