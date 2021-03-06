package journey.project.fragments

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentResolver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import journey.project.R
import journey.project.data.DbRepository
import kotlinx.android.synthetic.main.fragment_travel_note_info.*
import journey.project.models.TravelNote

class TravelNoteInfoFragment : Fragment(), OnMapReadyCallback {
    val travelNoteKey : String = "travelinfo"
    lateinit var mapView: MapView
    var dbRepository: DbRepository? = null
    var note : TravelNote? = null
    lateinit var navController: NavController
    var contentResolver : ContentResolver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbRepository = DbRepository(requireContext())
        val view = inflater.inflate(R.layout.fragment_travel_note_info, container, false)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        contentResolver = requireContext().contentResolver;

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    private fun updateWidget() {
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(requireContext(),JourneyTrackerWidget::class.java))

        val widgetPersonal = JourneyTrackerWidget()
        widgetPersonal.onUpdate(requireContext(), AppWidgetManager.getInstance(requireContext()),ids);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = arguments?.getSerializable(travelNoteKey) as TravelNote?
        textViewTitle.text = note?.Name
        textViewLocation.text = note?.Country + ", " + note?.Locality + ", " + note?.Address
        editTextNote.setText(note?.Note)


        buttonUpdate.setOnClickListener() {
            updateWidget()

            dbRepository?.updateNote(note!!)
            navController.navigate(R.id.allTravelNotesFragment)
        }

        buttonDelete.setOnClickListener() {
            updateWidget()

            dbRepository?.deleteNote(note?.LocationId!!)
            navController.navigate(R.id.allTravelNotesFragment)
        }
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
