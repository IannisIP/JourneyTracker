package journey.project.data

import android.database.Cursor
import androidx.room.*
import journey.project.models.TravelNote


@Dao
abstract class TravelDao {
    @Insert
    abstract fun insertNote(note: TravelNote) : Long

    @Transaction
    @Query("select * from travelnotes")
    abstract fun queryNotes() : MutableList<TravelNote>

    @Query("SELECT * FROM travelnotes")
    abstract fun selectAll(): Cursor?

    @Query("SELECT * FROM travelnotes WHERE LocationId=:id ")
    abstract fun loadSingle(id: Long): TravelNote

    @Query("DELETE FROM travelnotes WHERE LocationId=:id")
    abstract fun deleteById(id: Long): Int

    @Update
    abstract fun updateTravelNote(note: TravelNote): Int
}