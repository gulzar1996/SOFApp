package github.gulzar1996.overflowsearch.data.local.question

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import github.gulzar1996.overflowsearch.data.model.search.Question

@Database(
        entities = [(Question::class)],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun question(): QuestionDao

}