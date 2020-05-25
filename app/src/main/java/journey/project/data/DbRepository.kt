package journey.project.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import journey.project.models.TravelNote

class DbRepository(context: Context) {
    val dbContext: TravelDb
    val travelDao: TravelDao

    init {
        dbContext = TravelDb.getInstanta(context)!!
        travelDao = dbContext.getTravelDao()
    }

    fun getNotes(): MutableList<TravelNote> {
        return travelDao.queryNotes()
    }

    fun insertNote(note: TravelNote): Long {
        return travelDao.insertNote(note)
    }

    fun deleteNote(id: Long) {
        travelDao.deleteById(id)
    }

    fun updateNote(note: TravelNote){
        travelDao.updateTravelNote(note)
    }
}