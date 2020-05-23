package journey.project.googleplacesapi

import java.net.HttpURLConnection
import java.net.URL

class GooglePlaces {

    companion object {
        val APIKey : String = "AIzaSyDJ-4Uoqr5h35oWsH2OLNBgmwgXjelb06I"
        var result : String? = null
        fun  GetLocationSuggestions(
            keyword: String,
            latitude: String,
            longitude: String,
            radius: String
        ) : String? {

            var returned : String? = null
            Thread({
                URL(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location="
                            + latitude + "," + longitude +
                            "&radius=" + radius
                            + "&keyword=" + keyword
                            + "&key=" + APIKey
                ).run {
                    openConnection().run {
                        this as HttpURLConnection
                        returned = inputStream.bufferedReader().readText()
                    }
                }
            }).start()

           return ""
        }
    }
}