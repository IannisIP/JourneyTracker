package journey.project.data

import androidx.room.Embedded
import androidx.room.Relation
import journey.project.models.PunctGeo
import journey.project.models.Traseu
import java.io.Serializable

class TraseuCuPuncte (
    @Embedded
    val traseu : Traseu,
    @Relation(parentColumn = "id", entityColumn = "idTraseu")
    val coord : MutableList<PunctGeo>
    ) : Serializable