package journey.project.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//coordonatele asociate unui punct de pe traseu; am putea include si altitudinea
@Entity(tableName = "punct_geo")
data class PunctGeo(var latid : Double = 0.0,
                    var longit : Double = 0.0){

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var idTraseu: Long = 0
}

