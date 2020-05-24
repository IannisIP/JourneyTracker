package journey.project.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import journey.project.models.PunctGeo
import journey.project.models.Traseu
import journey.project.models.TravelNote

@Dao
abstract class TravelDao {
      @Insert
    abstract fun insertNote(note: TravelNote) : Long

    @Transaction
    @Query("select * from travelnotes")
    abstract fun queryNotes() : LiveData<MutableList<TravelNote>>

}