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
import com.google.android.gms.maps.model.PolylineOptions
import journey.project.R
import kotlinx.android.synthetic.main.fragment_detalii_traseu.*
import journey.project.data.TraseuCuPuncte
import journey.project.models.PunctGeo

//23.04.2020 - Detalii traseu selectat
class DetaliiTraseuFragment : Fragment(), OnMapReadyCallback {

    lateinit var mapView: MapView
    var traseu : TraseuCuPuncte? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detalii_traseu, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        traseu = arguments?.getSerializable("TRASEU") as TraseuCuPuncte?
        textViewInfoTraseu.text = traseu?.traseu?.denumire
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
        //TODO: de desenat traseul pe harta
        //operatiile cu harta: desenarea pe harta, afisare marcaje etc.
        //folosim PolylineOptions() si new
        //map. var traseu: Traseu = Traseu()
        var polylineOptions: PolylineOptions = PolylineOptions()

        traseu?.coord?.forEach { punctGeo: PunctGeo -> run {
            polylineOptions.add(LatLng(punctGeo.latid, punctGeo.longit))
        } }

        var firstPositon : PunctGeo? = traseu?.coord?.first()
        var lastPositon : PunctGeo? = traseu?.coord?.last()

        if (map != null) {
            val builder = LatLngBounds.Builder()
            if(firstPositon != null && lastPositon != null)
                builder.include(LatLng(lastPositon.latid, lastPositon.longit)).include(LatLng(firstPositon.latid, firstPositon.longit))
            val latLngBounds : LatLngBounds = builder.build()
            map.addPolyline(polylineOptions)
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,100));
        }
    }
}
