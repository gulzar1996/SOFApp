package github.gulzar1996.overflowsearch.data.local.question

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.NetworkState
import github.gulzar1996.overflowsearch.data.model.search.Listing
import github.gulzar1996.overflowsearch.data.model.search.Question
import github.gulzar1996.overflowsearch.data.model.search.QuestionSearch
import github.gulzar1996.overflowsearch.data.remote.SOFApi
import github.gulzar1996.overflowsearch.utils.toMd5Hash
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import javax.inject.Inject

class QuestionSearchRepository
@Inject constructor(private val db: AppDatabase
                    , private val sofApi: SOFApi
                    , private val ioExecutor: Executor)
    : IQuestionSearchRepository {


    var networkPageSize: Int = Const.DEFAULT_NETWORK_PAGE_SIZE

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertResultIntoDb(questionQuery: String, body: QuestionSearch?, pageCount: Int) {

        val hashedQuestionQuery = questionQuery.toMd5Hash()
        Log.d("QuestionSearch","Hash : $hashedQuestionQuery")
        body!!.questions.let { questions ->
            db.runInTransaction {
                val start = db.question().getNextIndexInQuestion(hashedQuestionQuery)
                val items = questions.mapIndexed { index, child ->
                    /**
                     * Some modifications in the question object before
                     * storing it into persistence
                     */
                    //Resolves author name from Owner Object
                    child.authorName = child.owner?.displayName.toString()
                    //Store query as hash for querying from sql
                    child.questionQuery = hashedQuestionQuery
                    //Maintain the order while storing
                    child.indexQ = start + index
                    //Store page count
                    child.currentPage = pageCount

                    Log.d("QuestionSearchRep", "Index :${child.indexQ} Page :${child.currentPage} ")
                    return@mapIndexed child
                }
                db.question().insert(items)
            }
        }
    }


    private fun refresh(questionQuery: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        sofApi.questionSearch(
                page = Const.INITIAL_PAGE,
                pagesize = networkPageSize.toString(),
                intitle = questionQuery,
                order = Const.ORDER,
                sort = Const.SORT,
                site = Const.SITE
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->

                    //Check if the response is successful
                    if (it.isSuccessful) {
                        ioExecutor.execute {
                            db.runInTransaction {
                                db.question().deleteQuestionsByQuery(questionQuery.toMd5Hash())
                                insertResultIntoDb(questionQuery, it.body(), Const.INITIAL_PAGE.toInt())
                            }
                        }
                        networkState.value = NetworkState.LOADED

                    } else
                        networkState.value = NetworkState.error("")

                }, { t ->
                    networkState.value = NetworkState.error(t.message)
                })
        return networkState
    }

    override fun getQuestionsByQuery(questionQuery: String, pageSize: Int): Listing<Question> {
        val boundaryCallback = QuestionSearchBoundaryCallback(
                questionQuery = questionQuery,
                handleResponse = this::insertResultIntoDb,
                ioExecutor = ioExecutor,
                networkPageSize = networkPageSize,
                sofApi = sofApi
        )

        val dataSourceFactory = db.question().questionByQuery(questionQuery.toMd5Hash())
        val builder = LivePagedListBuilder(dataSourceFactory, pageSize)
                .setBoundaryCallback(boundaryCallback)

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger, {
            refresh(questionQuery)
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


}