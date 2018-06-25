package github.gulzar1996.overflowsearch.data.local.answer

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.gulzar1996.overflowsearch.data.model.answer.Answer

@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Answer>)

    @Query("SELECT * FROM answer WHERE questionId = :questionId ORDER BY indexQ ASC")
    fun answerByQuery(questionId: String): DataSource.Factory<Int, Answer>

    @Query("DELETE FROM answer WHERE questionId = :questionId")
    fun deleteAnswers(questionId: String)

    @Query("SELECT MAX(indexQ) + 1 FROM answer WHERE questionId = :questionId")
    fun getNextIndexInAnswer(questionId: String): Int
}