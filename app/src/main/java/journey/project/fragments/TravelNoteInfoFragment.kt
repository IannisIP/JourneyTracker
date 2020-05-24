package journey.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import journey.project.R
import journey.project.data.DbRepository
import kotlinx.android.synthetic.main.fragment_travel_note_info.*
import journey.project.models.PunctGeo
import journey.project.models.TravelNote

class TravelNoteInfoFragment : Fragment(), OnMapReadyCallback {
    val travelNoteKey : String = "travelinfo"
    lateinit var mapView: MapView
    var dbRepository: DbRepository? = null
    var note : TravelNote? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbRepository = DbRepository(requireContext())
        val view = inflater.inflate(R.layout.fragment_travel_note_info, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = arguments?.getSerializable(travelNoteKey) as TravelNote?
        textViewTitle.text = note?.Name
        textViewLocation.text = note?.Country + ", " + note?.Locality + ", " + note?.Address
        editTextNote.setText(note?.Note)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()

    }
    override fun onMapReady(map: GoogleMap?) {
        if(note?.Latitude != null && note?.Longitude != null ) {
            var coord = LatLng(note?.Latitude!!, note?.Longitude!!)
            map?.addMarker(
                MarkerOptions().position(coord!!).title("Visited location")
            )
            map?.moveCamera(CameraUpdateFactory.newLatLng(coord))
            map?.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) )
        }
    }
}
