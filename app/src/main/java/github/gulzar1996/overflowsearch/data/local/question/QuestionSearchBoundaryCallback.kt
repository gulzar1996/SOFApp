package github.gulzar1996.overflowsearch.data.local.question

import android.arch.paging.PagedList
import github.gulzar1996.overflowsearch.Const
import github.gulzar1996.overflowsearch.data.Question
import github.gulzar1996.overflowsearch.data.QuestionSearch
import github.gulzar1996.overflowsearch.data.remote.SOFApi
import github.gulzar1996.overflowsearch.utils.PagingRequestHelper
import github.gulzar1996.overflowsearch.utils.createStatusLiveData
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.Executor

class QuestionSearchBoundaryCallback(private val questionQuery: String,
                                     private val sofApi: SOFApi,
                                     private val handleResponse: (String, QuestionSearch?) -> Unit,
                                     private val ioExecutor: Executor,
                                     private val networkPageSize: Int)
    : PagedList.BoundaryCallback<Question>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()
    var pageCounter = 0


    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            sofApi.questionSearch(
                    page = pageCounter.toString(),
                    pagesize = networkPageSize.toString(),
                    intitle = questionQuery,
                    order = Const.ORDER,
                    sort = Const.SORT,
                    site = Const.SITE
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createWebserviceCallback(it))

        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Question) {
        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            sofApi.questionSearch(
                    page = pageCounter.toString(),
                    pagesize = networkPageSize.toString(),
                    intitle = questionQuery,
                    order = Const.ORDER,
                    sort = Const.SORT,
                    site = Const.SITE
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(createWebserviceCallback(it))
        }
    }

    /**
     * every time it gets new items, boundary callback simply inserts them into the database and
     * paging library takes care of refreshing the list if necessary.
     */
    private fun insertItemsIntoDb(
            response: Response<QuestionSearch>,
            it: PagingRequestHelper.Request.Callback) {
        ioExecutor.execute {
            handleResponse(questionQuery, response.body())
            it.recordSuccess()
        }
        //Increment page on success
        incrementPage()
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback): SingleObserver<Response<QuestionSearch>> {
        return object : SingleObserver<Response<QuestionSearch>> {

            override fun onSuccess(t: Response<QuestionSearch>) {
                if (t.isSuccessful)
                    insertItemsIntoDb(t, it)
                else
                    it.recordFailure(Exception())
            }

            override fun onError(e: Throwable) {
                it.recordFailure(e)
            }

            override fun onSubscribe(d: Disposable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        }
    }

    private fun incrementPage() {
        pageCounter++
    }
}
