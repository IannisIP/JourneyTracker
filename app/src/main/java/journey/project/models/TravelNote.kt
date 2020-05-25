package journey.project.models

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName="travelnotes")
data class TravelNote(@PrimaryKey(autoGenerate=true)var LocationId: Long = 0) : Serializable {
    lateinit var Name: String
    lateinit var Note: String
    lateinit var Country: String
    lateinit var Address: String
    lateinit var Locality: String
    lateinit var  Date : Date
    var Latitude : Double = 0.0
    var Longitude : Double = 0.0
    var UserRating : Float = 0f

    constructor( locationId : Long = 0, name: String, note : String,
                 date : Date, lat: Double, long: Double, userRating: Float, address : String, country: String, locality : String) : this(locationId) {
        this.Name = name
        this.Note = note
        this.Date = date
        this.Latitude = lat
        this.Longitude = long
        this.UserRating = userRating
        this.Address = address
        this.Country= country
        this.Locality = locality
    }

    companion object {
        fun fromContentValues(values: ContentValues?): TravelNote? {
            val travelnote = TravelNote()
            if (values != null && values.containsKey("LocationId")) {
                travelnote.LocationId = values.getAsLong("LocationId")
            }
            if (values != null && values.containsKey("Name")) {
                travelnote.Name = values.getAsString("Name")
            }
            return travelnote
        }
    }
}
