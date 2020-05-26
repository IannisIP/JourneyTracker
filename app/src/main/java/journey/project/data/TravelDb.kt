package journey.project.data

import android.content.Context
import androidx.room.*
import journey.project.models.TravelNote

@Database(entities = arrayOf(TravelNote::class), version = 1)
@TypeConverters(Converter::class)
abstract class TravelDb : RoomDatabase() {
    abstract fun getTravelDao(): TravelDao

    companion object {
        @Volatile
        var instanta: TravelDb? = null

        @Synchronized
        fun getInstanta(context: Context): TravelDb? {
            if (instanta == null) {

                instanta = Room.databaseBuilder(context.applicationContext, TravelDb::class.java, "travel.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instanta
        }
    }
}