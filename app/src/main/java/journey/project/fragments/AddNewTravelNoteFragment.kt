package journey.project.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.fragment_traseu_nou.*
import journey.project.R
import journey.project.data.TraseuCuPuncte
import journey.project.models.PunctGeo
import journey.project.models.Traseu
import journey.project.services.Communicator
import journey.project.services.ServiciuLocalizare
import java.util.*

//TODO: de analizat comportamentul, atunci cind fragmentul nu este vizibil (de ex. s-a apasat pe butonul Home) si serviciul este pornit
//TODO: la revenirea in acest fragment, ar trebui, daca serviciul ruleaza, sa restauram starea (denumirea afisata, butonul start dezactivat, stop activat etc.)
//Initiere traseu nou
class AddNewTravelNoteFragment : Fragment() {
    val COD_PERMISUNE = 1
    lateinit var thisContext: Context
    var serviciuPornit = false
    var traseu: Traseu = Traseu()
    val puncteGeo = mutableListOf<PunctGeo>()
    lateinit var dataInceput: Date
    lateinit var comm: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        comm = activity as Communicator
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traseu_nou, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisContext = this.requireActivity()
        //daca nu exista, solicitam permisiunea ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                thisContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                thisContext as FragmentActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                COD_PERMISUNE
            )
        }

        btnStart.setOnClickListener() {
            dataInceput = Date()
            val intent = Intent(activity, ServiciuLocalizare::class.java)

            if (!serviciuPornit) {
                thisContext.startService(intent)
                //editText.text.appendln("Serviciul a fost pornit ...")
                Toast.makeText(thisContext, "Serviciul a pornit", Toast.LENGTH_LONG).show();
            }
            serviciuPornit = !serviciuPornit
        }

        btnStop.setOnClickListener() {
            val intent = Intent(activity, ServiciuLocalizare::class.java)

            if (serviciuPornit) {
                thisContext.stopService(intent)
                Toast.makeText(thisContext, "Serviciul a fost oprit", Toast.LENGTH_LONG).show()
                traseu.denumire = denTraseu.text.toString()
                //traseu.coord = puncteGeo
                traseu.dataInceput = dataInceput
                traseu.dataIncheiere = Date()
                //Toast.makeText(thisContext, traseu.toString(), Toast.LENGTH_SHORT).show()
            }
            serviciuPornit = !serviciuPornit

            var traseuCuPuncte = TraseuCuPuncte(traseu, puncteGeo)

            comm.passDataCom(traseuCuPuncte)
        }
    }

    override fun onPause() {
        super.onPause()
        thisContext.unregisterReceiver(receptorLocatie)
    }

    override fun onResume() {
        super.onResume()
        thisContext.registerReceiver(receptorLocatie, IntentFilter("ACTION_COORD"))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //daca permisiunea nu a afost acceptata, iesim din aplicatienu exista permisiunea ACCESS_FINE_LOCATION
        if (requestCode == COD_PERMISUNE && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                thisContext = this.requireActivity()
                AlertDialog.Builder(thisContext).setTitle("Atentie!")
                    .setMessage("Aplicatia ruleaza doar cu permisiunea ACCESS_FINE_LOCATION!")
                    .setPositiveButton("Ok") { _, _ -> (thisContext as FragmentActivity).finish() }
                    .show()
            }
        }
    }

    //receptor de mesaje
    //primesate coordonatele de la serviciu si le afiseaza prin intermediul controlului dedicat
    val receptorLocatie = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val coord = intent?.getStringExtra("COORD")
            //Toast.makeText(thisContext, coord ?: "Fara coordonate ...", Toast.LENGTH_SHORT).show()
            val punctGeo = PunctGeo()
            if (coord != null) {
                punctGeo.latid = coord.split(",")[0].toDouble()
                punctGeo.longit = coord.split(",")[1].toDouble()
                puncteGeo.add(punctGeo);

            };
        }
    }

}
