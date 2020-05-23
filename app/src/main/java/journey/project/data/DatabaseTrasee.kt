package journey.project.data

import android.content.Context
import androidx.room.*
import journey.project.models.PunctGeo
import journey.project.models.Traseu

@Database(entities = arrayOf(Traseu::class, PunctGeo::class), version = 1)
@TypeConverters(Conversii::class)
abstract class DatabaseTrasee : RoomDatabase() {
    abstract fun getDaoTrasee(): DaoTrasee

    companion object {
        @Volatile
        var instanta: DatabaseTrasee? = null

        @Synchronized
        fun getInstanta(context: Context): DatabaseTrasee? {
            if (instanta == null) {

                instanta = Room.databaseBuilder(context.applicationContext, DatabaseTrasee::class.java, "trasee.db")
                    .allowMainThreadQueries() //TODO:  de eliminat!!! apelurile la metodele din @Dao se vor realiza asincron!
                    .fallbackToDestructiveMigration() //gestiunea versiunilor (ulterior)
                    .build()
            }
            return instanta
        }
    }
}