package github.gulzar1996.overflowsearch.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "question")
data class Question(
        @SerializedName("tags") val tags: List<String>,
        @Ignore
        @SerializedName("owner") val owner: Owner,
        @SerializedName("is_answered") val isAnswered: Boolean,
        @SerializedName("view_count") val viewCount: Int,
        @SerializedName("answer_count") val answerCount: Int,
        @SerializedName("score") val score: Int,
        @SerializedName("creation_date") val creationDate: Int,
        @SerializedName("question_id") val questionId: Int,
        @SerializedName("link") val link: String,
        @SerializedName("title") val title: String
) {
    /**
     * Im not storing question Id as primary key because
     * two or more queries can have same question id and will overWrite
     * in the database causing loss of Question
     */
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    /**
     * Okay all the Query should be hashed and stored and
     * All the questions for a particular search term will be based on hashed question Query
     */
    var questionQuery = null
    /**
     * This is used to preserve the question order
     */
    var indexQ = -1
    /**
     * All the object should be appended with hasMore which is used during boundary call back
     */
    var hasMore: Boolean = false
    /**
     * Since its not wise to store the Owner object in Sql
     * Resolve it and store as string so that it is visible in a single question query
     */
    var authorName: String = ""
}