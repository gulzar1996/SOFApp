package github.gulzar1996.overflowsearch.data.model.answer

import com.google.gson.annotations.SerializedName

data class AnswerSearch(
        @SerializedName("items") val items: List<Answer>,
        @SerializedName("has_more") val hasMore: Boolean,
        @SerializedName("quota_max") val quotaMax: Int,
        @SerializedName("quota_remaining") val quotaRemaining: Int
)