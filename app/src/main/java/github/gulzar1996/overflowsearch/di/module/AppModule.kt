package github.gulzar1996.overflowsearch.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.data.local.question.AppDatabase
import github.gulzar1996.overflowsearch.data.local.question.QuestionSearchRepository
import github.gulzar1996.overflowsearch.data.remote.SOFApi
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    /**
     * Retrofit
     */
    @Provides
    @Singleton
    fun provideSOFApi(retrofit: Retrofit): SOFApi = retrofit.create(SOFApi::class.java)

    @Provides
    @Singleton
    internal fun provideRetrofitInterface(): Retrofit =
            Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()

    /**
     * Room
     */
    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "stack_overflow.db").build()

    @Provides
    @Singleton
    fun provideExecutor(): Executor = Executors.newSingleThreadExecutor()

    @Provides
    fun provideQuestionSearchRepo(db: AppDatabase, sofApi: SOFApi, ioExecutor: Executor) =
            QuestionSearchRepository(db, sofApi, ioExecutor)
//
//
//    @Provides
//    @Singleton
//    internal fun provideAnswerRepository(appDatabase: AppDatabase): AnswerRepository =
//            AnswerRepository(appDatabase.answerDao())
//
//    @Provides
//    @Singleton
//    internal fun providesAnswerDao(appDatabase: AppDatabase): AnswerDao = appDatabase.answerDao()
//
//    @Provides
//    @Singleton
//    internal fun provideUserRepository(appDatabase: AppDatabase): UserRepository =
//            UserRepository(appDatabase.userDao())
//
//    @Provides
//    @Singleton
//    internal fun providesUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()
//
//    @Provides
//    @Singleton
//    internal fun provideQuestionRepository(appDatabase: AppDatabase): QuestionRepository =
//            QuestionRepository(appDatabase.questionDao())
//
//    @Provides
//    @Singleton
//    internal fun providesQuestionDao(appDatabase: AppDatabase): QuestionDao = appDatabase.questionDao()

    /**
     * Shared Preferences
     */

    /**
     * Rx
     */

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()


}