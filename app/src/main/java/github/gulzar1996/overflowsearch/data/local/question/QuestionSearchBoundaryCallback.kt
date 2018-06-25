package github.gulzar1996.overflowsearch.data.local.question

import android.arch.paging.PagedList
import android.util.Log
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.data.model.search.Question
import github.gulzar1996.overflowsearch.data.model.search.QuestionSearch
import github.gulzar1996.overflowsearch.data.remote.SOFApi
import github.gulzar1996.overflowsearch.utils.PagingRequestHelper
import github.gulzar1996.overflowsearch.utils.createStatusLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.Executor
import kotlin.reflect.KFunction3

class QuestionSearchBoundaryCallback(private val questionQuery: String,
                                     private val sofApi: SOFApi,
                                     private val handleResponse: KFunction3<@ParameterName(name = "questionQuery") String, @ParameterName(name = "body") QuestionSearch?, @ParameterName(name = "pageCount") Int, Unit>,
                                     private val ioExecutor: Executor,
                                     private val networkPageSize: Int)
    : PagedList.BoundaryCallback<Question>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()


    override fun onZeroItemsLoaded() {
        Log.i("onZeroItemsLoaded", "called")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            sofApi.questionSearch(
                    page = Const.INITIAL_PAGE,
                    pagesize = networkPageSize.toString(),
                    intitle = questionQuery,
                    order = Const.ORDER,
                    sort = Const.SORT,
                    site = Const.SITE
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe({ resp ->
                        //If empty list is found in the first load show approprate result
                        if (resp.isSuccessful) {
                            if (resp.body()!!.questions.isEmpty())
                                failedWebService(it, Exception("$questionQuery Not Found"))
                            else
                                successfulWebservice(it, resp, pageCount = Const.INITIAL_PAGE.toInt())
                        } else
                            failedWebService(it, Exception("Rate Limited"))

                    }, { t ->
                        failedWebService(it, t)
                    })

        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Question) {

        Log.i("onItemAtEndLoaded", "called")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            sofApi.questionSearch(
                    page = (itemAtEnd.currentPage + 1).toString(),
                    pagesize = networkPageSize.toString(),
                    intitle = questionQuery,
                    order = Const.ORDER,
                    sort = Const.SORT,
                    site = Const.SITE
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ resp ->
                        successfulWebservice(it, resp, pageCount = itemAtEnd.currentPage + 1)
                    }, { t ->
                        failedWebService(it, t)
                    })
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(
            response: Response<QuestionSearch>,
            it: PagingRequestHelper.Request.Callback,
            pageCount: Int) {
        ioExecutor.execute {
            handleResponse(questionQuery, response.body(), pageCount)
            it.recordSuccess()
        }
    }

    private fun successfulWebservice(it: PagingRequestHelper.Request.Callback, response: Response<QuestionSearch>
                                     , pageCount: Int) {
        if (response.isSuccessful) {
            if (!response.body()!!.questions.isEmpty())
                insertItemsIntoDb(response, it, pageCount)
            else
                it.recordFailure(Exception())
        } else it.recordFailure(Exception())
    }

    private fun failedWebService(it: PagingRequestHelper.Request.Callback, t: Throwable) {
        it.recordFailure(t)
    }


}
