package journey.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_trasee.*
import journey.project.R
import journey.project.adapters.TraseeAdapter
import journey.project.data.DatabaseTrasee
import journey.project.data.TraseuCuPuncte
import java.util.*

//Fragmentul principal; afiseaza lista traseele definite
class TraseeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trasee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val traseuNou: Traseu? = arguments?.getSerializable("traseu") as Traseu;
        val data = Date()
        val calendar = Calendar.getInstance()
        calendar.time =  data

        val listaTrasee: MutableList<TraseuCuPuncte>? = DatabaseTrasee.getInstanta(requireContext())?.getDaoTrasee()?.selecteazaToateTraseele()
        /*
        val listaTrasee : MutableList<Traseu> = mutableListOf<Traseu>()
        listaTrasee.clear()

        if (traseuNou != null) {
            listaTrasee.add(traseuNou)
        }

         */
        val traseeAdapter = TraseeAdapter(listaTrasee!!)
                //asociere adaptro
        recylerTrasee.adapter = traseeAdapter
        //lista verticala
        recylerTrasee.layoutManager = LinearLayoutManager(context)
        //separator elemente
        recylerTrasee.addItemDecoration(DividerItemDecoration(context,
            DividerItemDecoration.VERTICAL))


    }
}
