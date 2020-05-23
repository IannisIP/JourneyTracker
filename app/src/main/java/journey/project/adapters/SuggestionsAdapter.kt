package journey.project.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import journey.project.R
import journey.project.data.TraseuCuPuncte
import journey.project.models.LocationSuggestion
import java.text.SimpleDateFormat
import java.util.*

class SuggestionsAdapter(private val suggestionsList: MutableList<LocationSuggestion>) : RecyclerView.Adapter<SuggestionsAdapter.VH>()  {

    lateinit var recycler: RecyclerView
    val bundleKey : String = "suggestion"
    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val textViewLocationName: TextView = view.findViewById(R.id.textViewLocationName)
        val textViewLocationVecinity: TextView = view.findViewById(R.id.textViewLocationVecinity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linie_traseu, parent, false)

        view.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                val index = recycler.getChildLayoutPosition(view)
                val bundle = Bundle()
                bundle.putSerializable(bundleKey, suggestionsList[index])
                v?.findNavController()?.navigate(R.id.travelNoteFragment, bundle)
            }

        })
        return VH(view)
    }

    override fun getItemCount(): Int {
        return suggestionsList.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }


    override fun onBindViewHolder(holder: VH, index: Int) {
        val location: LocationSuggestion = suggestionsList[index]

        holder.textViewLocationName.text = location.Name
        holder.textViewLocationVecinity.text = location.Vecinity
    }


}