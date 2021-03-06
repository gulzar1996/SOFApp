package github.gulzar1996.overflowsearch.data.model.answer

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "answer")
data class Answer(
        @Ignore
        @SerializedName("owner") var owner: Owner? = null,
        @SerializedName("is_accepted") var isAccepted: Boolean? = null,
        @SerializedName("score") var score: Int? = null,
        @SerializedName("last_activity_date") var lastActivityDate: Int? = null,
        @SerializedName("creation_date") var creationDate: Int? = null,
        @SerializedName("answer_id") var answerId: Int? = null,
        @SerializedName("question_id") var questionId: Int? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("body") var body: String? = null
) {
    /**
     * Im not storing question Id as primary key because
     * two or more queries can have same question id and will overWrite
     * in the database causing loss of Question
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    /**
     * This is used to preserve the question order
     */
    var indexQ = -1
    /**
     * All the object should be appended with hasMore which is used during boundary call back
     */
    var hasMore: Boolean = false
    var currentPage: Int = -1
    /**
     * Since its not wise to store the Author object in Sql
     * Resolve it and store as string so that it is visible in a single question query
     */
    var authorName: String = ""
}