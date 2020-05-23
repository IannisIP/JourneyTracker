package journey.project.data

import androidx.room.TypeConverter
import java.util.*

class Conversii {
    @TypeConverter
    fun dateToLong(date: Date) : Long {
        return date.time
    }
    @TypeConverter
    fun dateToLong(date: Long) : Date {
        return Date(date)
    }
}