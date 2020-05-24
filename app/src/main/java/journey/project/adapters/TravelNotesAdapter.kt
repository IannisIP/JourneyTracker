package journey.project.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import journey.project.R
import journey.project.models.TravelNote
import java.text.SimpleDateFormat
import java.util.*

class TravelNotesAdapter(private val travelNotes: MutableList<TravelNote>) : RecyclerView.Adapter<TravelNotesAdapter.VH>() {

    val travelNoteKey : String = "travelinfo"

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val textViewLocationName: TextView = view.findViewById(R.id.textViewLocationName)
        val textViewNoteDate: TextView = view.findViewById(R.id.textViewNoteDate)
        val textViewLocation: TextView = view.findViewById(R.id.textViewCountry)
        val ratingBar: RatingBar = view.findViewById(R.id.locationRating)
    }

    lateinit var recycler: RecyclerView
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview_item, parent, false)

        view.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val index = recycler.getChildLayoutPosition(view)
                val bundle = Bundle()
                bundle.putSerializable(travelNoteKey, travelNotes[index])
                v?.findNavController()?.navigate(R.id.travelNoteInfoFragment, bundle)
            }

        })
        return VH(view)
    }

    override fun getItemCount(): Int {
        return travelNotes.size
    }

    override fun onBindViewHolder(holder: VH, index: Int) {
        val note: TravelNote = travelNotes[index]
        holder.textViewLocationName.text = note.Name
        holder.textViewNoteDate.text = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(note.Date)
        holder.textViewLocation.text = note.Country + ", " + note.Locality + ", "+note.Address
        holder.ratingBar.isEnabled = false
        holder.ratingBar.setRating(note.UserRating)
    }
}