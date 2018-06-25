package github.gulzar1996.overflowsearch.data.model.answer

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "question")
data class Answer(
        @SerializedName("owner") val owner: Owner,
        @SerializedName("is_accepted") val isAccepted: Boolean,
        @SerializedName("score") val score: Int,
        @SerializedName("last_activity_date") val lastActivityDate: Int,
        @SerializedName("creation_date") val creationDate: Int,
        @SerializedName("answer_id") val answerId: Int,
        @SerializedName("question_id") val questionId: Int,
        @SerializedName("title") val title: String,
        @SerializedName("body") val body: String
)