package journey.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_all_travel_notes.*
import journey.project.R
import journey.project.adapters.TravelNotesAdapter
import journey.project.data.TravelDb
import java.util.*

//Fragmentul principal; afiseaza lista traseele definite
class AllTravelNotesFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_travel_notes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val traseuNou: Traseu? = arguments?.getSerializable("traseu") as Traseu;
        val data = Date()
        val calendar = Calendar.getInstance()
        calendar.time =  data
//
//        val listaTrasee: MutableList<TraseuCuPuncte>? = TravelDb.getInstanta(requireContext())?.getDaoTrasee()?.selecteazaToateTraseele()
//
//        val traseeAdapter = TravelNotesAdapter(listaTrasee!!)
//                //asociere adaptro
//        recylerTrasee.adapter = traseeAdapter
//        //lista verticala
//        recylerTrasee.layoutManager = LinearLayoutManager(context)
//        //separator elemente
//        recylerTrasee.addItemDecoration(DividerItemDecoration(context,
//            DividerItemDecoration.VERTICAL))


    }
}