package github.gulzar1996.overflowsearch.data.model.search

import com.google.gson.annotations.SerializedName

data class QuestionSearch(
        @SerializedName("items") val questions: List<Question>,
        @SerializedName("has_more") val hasMore: Boolean,
        @SerializedName("quota_max") val quotaMax: Int,
        @SerializedName("quota_remaining") val quotaRemaining: Int
)