package journey.project.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import journey.project.R
import journey.project.googleplacesapi.GooglePlaces


class PlacesSuggestionFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sugestionslist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val suggestionList = GooglePlaces.GetLocationSuggestions("",  "44.177269", "28.652880","500" )
//        val adapter = SuggestionsAdapter(suggestionList!!)
//
//        recylerTrasee.adapter = adapter
//        recylerTrasee.layoutManager = LinearLayoutManager(context)
//        recylerTrasee.addItemDecoration(DividerItemDecoration(context,
//            DividerItemDecoration.VERTICAL))
    }
}
