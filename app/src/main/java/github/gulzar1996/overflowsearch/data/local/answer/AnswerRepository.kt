package github.gulzar1996.overflowsearch.data.local.answer

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.Const.INITIAL_PAGE
import github.gulzar1996.overflowsearch.NetworkState
import github.gulzar1996.overflowsearch.data.local.question.AppDatabase
import github.gulzar1996.overflowsearch.data.model.answer.Answer
import github.gulzar1996.overflowsearch.data.model.answer.AnswerSearch
import github.gulzar1996.overflowsearch.data.model.search.Listing
import github.gulzar1996.overflowsearch.data.model.search.Question
import github.gulzar1996.overflowsearch.data.remote.SOFApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import javax.inject.Inject

class AnswerRepository
@Inject constructor(private val db: AppDatabase
                    , private val sofApi: SOFApi
                    , private val ioExecutor: Executor) : IAnswerRepository {


    var networkPageSize: Int = Const.DEFAULT_NETWORK_PAGE_SIZE

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertResultIntoDb(questionId: String, body: AnswerSearch?, pageCount: Int) {


        body!!.answers.let { answers ->
            db.runInTransaction {
                val start = db.answer().getNextIndexInAnswer(questionId)
                val items = answers.mapIndexed { index, child ->
                    /**
                     * Some modifications in the answer object before
                     * storing it into persistence
                     */
                    //Resolves author name from Owner Object
                    child.authorName = child.owner?.displayName.toString()
                    //Maintain the order while storing
                    child.indexQ = start + index
                    //Store page count
                    child.currentPage = pageCount

                    Log.d("AnswerRepos", "Index :${child.indexQ} Page :${child.currentPage} ")
                    return@mapIndexed child
                }
                db.answer().insert(items)
            }
        }
    }


    private fun refresh(questionId: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        sofApi.getAnswers(
                page = Const.INITIAL_PAGE,
                pagesize = networkPageSize.toString(),
                questionId = questionId,
                order = Const.ORDER,
                sort = Const.SORT,
                site = Const.SITE,
                filter = Const.ANSWER_FILTER
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->

                    //Check if the response is successful
                    if (it.isSuccessful) {
                        ioExecutor.execute {
                            db.runInTransaction {
                                db.answer().deleteAnswers(questionId)
                                insertResultIntoDb(
                                        questionId = questionId
                                        , body = it.body()
                                        , pageCount = INITIAL_PAGE.toInt())
                            }
                        }
                        networkState.value = NetworkState.LOADED

                    } else
                        networkState.value = NetworkState.error("No Answers")

                }, { t ->
                    networkState.value = NetworkState.error(t.message)
                })
        return networkState
    }

    override fun getAnswers(questionId: String, pageSize: Int): Listing<Answer> {
        val boundaryCallback = AnswerBoundaryCallback(
                questionId = questionId,
                handleResponse = this::insertResultIntoDb,
                ioExecutor = ioExecutor,
                networkPageSize = networkPageSize,
                sofApi = sofApi
        )

        val dataSourceFactory = db.answer().answerByQuery(questionId)
        val builder = LivePagedListBuilder(dataSourceFactory, pageSize)
                .setBoundaryCallback(boundaryCallback)

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger, {
            refresh(questionId = questionId)
        })

        return Listing(
                pagedList = builder.build(),
                networkState = boundaryCallback.networkState,
                retry = {
                    boundaryCallback.helper.retryAllFailed()
                },
                refresh = {
                    refreshTrigger.value = null
                },
                refreshState = refreshState
        )

    }

    override fun getQuestion(questionId: String): LiveData<Question> {
        return db.question().getQuestion(questionId.toInt())
    }


}