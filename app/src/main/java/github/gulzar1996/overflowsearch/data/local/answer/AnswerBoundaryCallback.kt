package github.gulzar1996.overflowsearch.data.local.answer

import android.arch.paging.PagedList
import android.util.Log
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.data.model.answer.Answer
import github.gulzar1996.overflowsearch.data.model.answer.AnswerSearch
import github.gulzar1996.overflowsearch.data.remote.SOFApi
import github.gulzar1996.overflowsearch.utils.PagingRequestHelper
import github.gulzar1996.overflowsearch.utils.createStatusLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.Executor

class AnswerBoundaryCallback(private val questionId: String,
                             private val sofApi: SOFApi,
                             private val handleResponse: (String, AnswerSearch?, Int) -> Unit,
                             private val ioExecutor: Executor,
                             private val networkPageSize: Int)
    : PagedList.BoundaryCallback<Answer>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()


    override fun onZeroItemsLoaded() {
        Log.i("onZeroItemsLoaded", "called")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
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
                    .subscribe({ resp ->
                        //If empty list is found in the first load show approprate result
                        if (resp.isSuccessful && resp.body()!!.answers.isEmpty())
                            failedWebService(it, Exception("No Answers"))
                        else
                            successfulWebservice(it, resp, pageCount = Const.INITIAL_PAGE.toInt())
                    }, { t ->
                        failedWebService(it, t)
                    })

        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Answer) {

        Log.i("onItemAtEndLoaded", "called")

        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            sofApi.getAnswers(
                    page = (itemAtEnd.currentPage + 1).toString(),
                    pagesize = networkPageSize.toString(),
                    questionId = questionId,
                    order = Const.ORDER,
                    sort = Const.SORT,
                    site = Const.SITE,
                    filter = Const.ANSWER_FILTER
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
            response: Response<AnswerSearch>,
            it: PagingRequestHelper.Request.Callback,
            pageCount: Int) {
        ioExecutor.execute {
            handleResponse(questionId, response.body(), pageCount)
            it.recordSuccess()
        }
    }

    private fun successfulWebservice(it: PagingRequestHelper.Request.Callback, response: Response<AnswerSearch>
                                     , pageCount: Int) {
        if (response.isSuccessful) insertItemsIntoDb(response, it, pageCount) else it.recordFailure(Exception())
    }

    private fun failedWebService(it: PagingRequestHelper.Request.Callback, t: Throwable) {
        it.recordFailure(t)
    }


}
