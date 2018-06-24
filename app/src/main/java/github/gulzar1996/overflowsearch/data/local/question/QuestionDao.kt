package github.gulzar1996.overflowsearch.data.local.question

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.gulzar1996.overflowsearch.data.Question

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Question>)

    @Query("SELECT * FROM question WHERE questionQuery = :questionQuery ORDER BY indexQ ASC")
    fun questionByQuery(questionQuery: String): DataSource.Factory<Int, Question>

    @Query("DELETE FROM question WHERE questionQuery = :questionQuery")
    fun deleteQuestionsByQuery(questionQuery: String)

    @Query("SELECT MAX(indexQ) + 1 FROM question WHERE questionQuery = :questionQuery")
    fun getNextIndexInQuestion(questionQuery: String): Int
}