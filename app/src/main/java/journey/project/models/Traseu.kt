package journey.project.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*
//clasa asociata unui traseu; include o lista de coordonate
@Entity(tableName="trasee")
data class Traseu(@PrimaryKey(autoGenerate=true)var id: Long = 0): Serializable {
    lateinit var denumire: String
    lateinit var dataInceput: Date
    lateinit var dataIncheiere : Date
    //var coord : MutableList<PunctGeo>  =  mutableListOf<PunctGeo>()

    constructor( id : Long = 0, denumire: String, dataInceput: Date) : this(id) {
        this.denumire = denumire
        this.dataInceput = dataInceput
    }

    override fun toString(): String {
        return "Traseu(id=$id, denumire='$denumire', dataInceput=$dataInceput, dataIncheiere=$dataIncheiere)"
    }
}