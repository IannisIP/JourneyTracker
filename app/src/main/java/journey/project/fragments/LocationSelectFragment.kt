package journey.project.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import journey.project.R
import journey.project.services.ServiciuLocalizare
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_location_selector.*
import kotlinx.android.synthetic.main.fragment_traseu_nou.*
import java.util.*


class LocationSelectFragment : Fragment(), OnMapReadyCallback {

    lateinit var mapView: MapView
    lateinit var thisContext: Context
    var serviciuPornit = false
    var currentUserLoation : LatLng? = null
    lateinit var navController: NavController
    var country = "Country not detected."
    var address = "Address not detected."
    var locality = "Locality not detected."

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        val view = inflater.inflate(R.layout.fragment_location_selector, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thisContext = this.requireActivity()
        if (ContextCompat.checkSelfPermission(
                thisContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(thisContext as FragmentActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        val intent = Intent(activity, ServiciuLocalizare::class.java)

        btn_confirm_location.text = getString(R.string.confirm_location)

        btn_confirm_location.setOnClickListener() {
            val bundle = Bundle()
            bundle.putParcelable("currentUserLoation", currentUserLoation)
            bundle.putString("address", address)
            bundle.putString("locality", locality)
            bundle.putString("country", country)

            navController.navigate(R.id.newTravelFragment, bundle)
        }

        if (!serviciuPornit) {
            thisContext.startService(intent)
            serviciuPornit = !serviciuPornit
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        val intent = Intent(activity, ServiciuLocalizare::class.java)

        if (!serviciuPornit) {
            thisContext.startService(intent)
            Toast.makeText(thisContext, "Serviciul a pornit", Toast.LENGTH_LONG).show()
            serviciuPornit = !serviciuPornit
        }
        thisContext.registerReceiver(receptorLocatie, IntentFilter("ACTION_COORD"))
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        val intent = Intent(activity, ServiciuLocalizare::class.java) //Serviciu

        if (serviciuPornit) {
            thisContext.stopService(intent)
            Toast.makeText(thisContext, "Serviciul a fost oprit", Toast.LENGTH_LONG).show()
            serviciuPornit = !serviciuPornit
        }
        thisContext.unregisterReceiver(receptorLocatie)

    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    //receptor de mesaje
    //primesate coordonatele de la serviciu si le afiseaza prin intermediul controlului dedicat
    val receptorLocatie = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val coord = intent?.getStringExtra("COORD")
            val adrs = intent?.getStringExtra("Address")
            val localy = intent?.getStringExtra("Locality")
            val ctry = intent?.getStringExtra("Country")

            if (coord != null) {
                currentUserLoation = LatLng(coord.split(",")[0].toDouble(), coord.split(",")[1].toDouble())
            };
            if(adrs != null) {
                address = adrs
            }
            if(localy != null) {
                locality = localy
            }
            if(ctry != null) {
                country = ctry
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map != null) {
            map.setOnMapClickListener(GoogleMap.OnMapClickListener {
                map.clear()
                map.addMarker(MarkerOptions().position(it).title("Current location"))
                map.moveCamera(CameraUpdateFactory.newLatLng(it))
                map.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) )

                currentUserLoation = it
            })


            Handler().postDelayed(
                {
                    if(currentUserLoation != null) {
                        map.addMarker(
                            MarkerOptions().position(currentUserLoation!!).title("Current location")
                        )
                        map.moveCamera(CameraUpdateFactory.newLatLng(currentUserLoation))
                        map.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) )
                    }
                },2000
            )
        }
    }
}
