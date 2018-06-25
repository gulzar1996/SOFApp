package github.gulzar1996.overflowsearch.data.local.question

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import github.gulzar1996.overflowsearch.data.local.answer.AnswerDao
import github.gulzar1996.overflowsearch.data.model.answer.Answer
import github.gulzar1996.overflowsearch.data.model.search.Question

@Database(
        entities = [(Question::class), (Answer::class)],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun question(): QuestionDao
    abstract fun answer(): AnswerDao

}