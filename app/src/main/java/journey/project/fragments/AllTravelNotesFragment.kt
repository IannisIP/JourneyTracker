package journey.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_all_travel_notes.*
import journey.project.R
import journey.project.adapters.TravelNotesAdapter
import journey.project.data.DbRepository
import journey.project.data.TravelDb
import journey.project.models.TravelNote
import java.util.*

class AllTravelNotesFragment : Fragment() {
    var dbRepository: DbRepository? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dbRepository = DbRepository(requireContext())
        return inflater.inflate(R.layout.fragment_all_travel_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesList: MutableList<TravelNote> = dbRepository!!.getNotes()
        val adapter = TravelNotesAdapter(notesList)

        recylerTrasee.adapter = adapter
        recylerTrasee.layoutManager = LinearLayoutManager(context)
        recylerTrasee.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))
    }
}
