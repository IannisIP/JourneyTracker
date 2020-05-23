package journey.project.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import journey.project.models.PunctGeo
import journey.project.models.Traseu

@Dao
abstract class DaoTrasee {
    @Insert
    abstract fun insereazaTraseu(traseu: Traseu) : Long
    @Insert
    abstract fun insereazaPunct(punct: PunctGeo) : Long

    @Transaction
    @Query("select * from trasee")
    abstract fun selecteazaToateTraseele() : MutableList<TraseuCuPuncte>

    @Transaction
    @Insert
    fun insereazaTraseuCuPuncte(traseuCuPuncte: TraseuCuPuncte): Long {
        val id: Long = insereazaTraseu(traseuCuPuncte.traseu)
        for(punct:PunctGeo in traseuCuPuncte.coord) {
            punct.idTraseu = id
            insereazaPunct(punct)
        }
        return id
    }
}