package github.gulzar1996.overflowsearch.data.remote

import github.gulzar1996.overflowsearch.data.model.search.QuestionSearch
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SOFApi {

    @GET("search")
    fun questionSearch(
            @Query("page") page: String,
            @Query("pagesize") pagesize: String,
            @Query("order") order: String,
            @Query("sort") sort: String,
            @Query("intitle") intitle: String,
            @Query("site") site: String
    ): Single<Response<QuestionSearch>>

    @GET("/questions/{questionId}/answers")
    fun getAnswers(
            @Path("questionId") questionId: String,
            @Query("order") order: String,
            @Query("sort") sort: String,
            @Query("site") site: String,
            @Query("filter") filter: String
    )

}