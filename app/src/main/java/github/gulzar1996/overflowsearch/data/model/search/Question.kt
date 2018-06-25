package github.gulzar1996.overflowsearch.data.model.search

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "question")
data class Question(
        @Ignore
        @SerializedName("owner") var owner: Author? = null,
        @SerializedName("is_answered") var isAnswered: Boolean = false,
        @SerializedName("view_count") var viewCount: Int = 0,
        @SerializedName("answer_count") var answerCount: Int = 0,
        @SerializedName("score") var score: Int = 0,
        @SerializedName("creation_date") var creationDate: Long = 0,
        @SerializedName("question_id") var questionId: Int = 0,
        @SerializedName("link") var link: String? = null,
        @SerializedName("title") var title: String? = null
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
    var questionQuery = ""
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