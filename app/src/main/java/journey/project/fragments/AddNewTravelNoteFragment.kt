package journey.project.fragments

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import journey.project.R
import journey.project.data.DbRepository
import journey.project.models.TravelNote
import kotlinx.android.synthetic.main.fragment_add_travel_note.*
import java.util.*

class AddNewTravelNoteFragment : Fragment() {
    lateinit var thisContext: Context
    lateinit var navController: NavController
    var dbRepository: DbRepository? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        return inflater.inflate(R.layout.fragment_add_travel_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = this.requireActivity()
        dbRepository = DbRepository(thisContext)

        editTextName.hint = getString(R.string.hint_location_name)
        editTextNotes.hint = getString(R.string.hint_notes)

        val coordinates = arguments?.getParcelable("currentUserLoation") as LatLng?
        val country = arguments?.getString("country")
        val address = arguments?.getString("address")
        val locality = arguments?.getString("locality")
 
        btnSave.setOnClickListener(){
            val note = TravelNote()
            note.Name = editTextName.text.toString()
            note.Note = editTextNotes.text.toString()
            note.Date = Date()
            note.UserRating = ratingBar.rating
            note.Latitude = coordinates?.latitude!!
            note.Longitude = coordinates?.longitude!!
            note.Country = country!!
            note.Address = address!!
            note.Locality = locality!!

            dbRepository!!.insertNote(note)

            val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(requireContext(),JourneyTrackerWidget::class.java))

            val widgetPersonal = JourneyTrackerWidget()
            widgetPersonal.onUpdate(requireContext(), AppWidgetManager.getInstance(requireContext()),ids);

            val navOption = NavOptions.Builder().setPopUpTo(R.id.allTravelNotesFragment, true).build()
            navController.navigate(R.id.allTravelNotesFragment, null, navOption)
        }
    }

}
