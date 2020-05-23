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
import java.text.SimpleDateFormat
import java.util.*

//Adaptorul pentru lista traseelor (RecyclerView)
class TraseeAdapter(private val trasee: MutableList<TraseuCuPuncte>) : RecyclerView.Adapter<TraseeAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDenumire: TextView = view.findViewById(R.id.textViewDenumire)
        val textViewDataInceput: TextView = view.findViewById(R.id.textViewDataInceput)
        val textViewDataIncheiere: TextView = view.findViewById(R.id.textViewDataIncheiere)
        val textViewDurata: TextView = view.findViewById(R.id.textViewDurata)
        val textViewViteza: TextView = view.findViewById(R.id.textViewViteza)
    }

    lateinit var recycler: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recycler = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        //23.04.2020 - Evenimentul click pe element din lista
        val view = LayoutInflater.from(parent.context).inflate(R.layout.linie_traseu, parent, false)

        view.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                val pozitie = recycler.getChildLayoutPosition(view)
                val bundle = Bundle()
                bundle.putSerializable("TRASEU", trasee[pozitie])
                v?.findNavController()?.navigate(R.id.detaliiTraseuFragment, bundle)
            }

        })
        return VH(view)
    }

    override fun getItemCount(): Int {
        return trasee.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val traseuCuPuncte: TraseuCuPuncte = trasee[position]

        holder.textViewDenumire.text = traseuCuPuncte.traseu.denumire
        holder.textViewDataInceput.text =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(traseuCuPuncte.traseu.dataInceput)
        holder.textViewDataIncheiere.text = SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss",
            Locale.getDefault()
        ).format(traseuCuPuncte.traseu.dataIncheiere)
    }
}