package github.gulzar1996.overflowsearch

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import github.gulzar1996.overflowsearch.di.component.DaggerAppComponent

import javax.inject.Inject

class OverFlowSearchApp: Application(), HasActivityInjector {

    @Inject
    lateinit internal var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>


    override fun activityInjector() = activityDispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }

}
