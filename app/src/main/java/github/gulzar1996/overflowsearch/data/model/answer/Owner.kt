package github.gulzar1996.overflowsearch.data.model.answer

import com.google.gson.annotations.SerializedName

data class Owner(
        @SerializedName("reputation") val reputation: Int,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("user_type") val userType: String,
        @SerializedName("profile_image") val profileImage: String,
        @SerializedName("display_name") val displayName: String,
        @SerializedName("link") val link: String
)