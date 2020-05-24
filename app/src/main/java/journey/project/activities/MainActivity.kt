package journey.project.activities

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import journey.project.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    lateinit var navController: NavController
    lateinit var coordinatorLayout: CoordinatorLayout
    private var mSensorManager: SensorManager? = null
    private var mTemperature: Sensor? = null
    private val NOT_SUPPORTED_MESSAGE = "Sorry, temperature sensor not available for this device."
    var snackbar : Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer)
        navigationView.setNavigationItemSelectedListener(this)

        fab.setOnClickListener { view ->
            fab.setVisibility(View.GONE)
            navController.navigate(R.id.suggestionsFragment)
        }

        coordinatorLayout = findViewById(R.id.coordinatorLayout)

        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?;
        mTemperature= mSensorManager?.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        snackbar = Snackbar.make(coordinatorLayout, "Not yet computed", Snackbar.LENGTH_INDEFINITE)
        
        if (mTemperature == null) {
            setMessageOnSnackBar(NOT_SUPPORTED_MESSAGE)
        }
        snackbar!!.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer.closeDrawers()

        val id = item.itemId

        when (id) {
            R.id.action_trasee -> {
                fab.setVisibility(View.VISIBLE)
                navController.navigate(R.id.allTravelNotesFragment)
                return true
            }
            R.id.action_traseu_nou -> {
                fab.setVisibility(View.GONE)
                navController.navigate(R.id.newTravelFragment)
                return true
            }
            R.id.action_despre -> {
                fab.setVisibility(View.GONE)
                navController.navigate(R.id.despreFragment)
                return true
            }
            R.id.action_setari -> {
                fab.setVisibility(View.GONE)
                navController.navigate(R.id.setariFragment)
                return true
            }
        }

        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        fab.setVisibility(View.VISIBLE)
        return NavigationUI.navigateUp(navController, drawer);
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            fab.setVisibility(View.VISIBLE)
            super.onBackPressed();
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val ambient_temperature = event!!.values[0]
        var smartWeatherMessage: String = getSmartMessage(ambient_temperature)
        setMessageOnSnackBar(smartWeatherMessage)
    }

    private fun getSmartMessage(ambientTemperature: Float): String {
            var message : String = "";
            if(ambientTemperature < 0)
                message = "It's very cold, wear a coat and some gloves " + ambientTemperature.toString() + resources.getString(R.string.celsius)
            else if (ambientTemperature < 5)
                message = "It's cold, wear a coat and a scarf " + ambientTemperature.toString() + resources.getString(R.string.celsius)
            else if (ambientTemperature < 15)
                message = "It's chilly, wear a top and some jeans " + ambientTemperature.toString() + resources.getString(R.string.celsius)
            else if (ambientTemperature < 25)
                message = "It's warm, wear a t-shirt " + ambientTemperature.toString() + resources.getString(R.string.celsius)
            else
                message = "It's really hot, wear light t-shirt and shorts " + ambientTemperature.toString() + resources.getString(R.string.celsius)

        return message
    }

    fun setMessageOnSnackBar(message: String) {
        snackbar?.setText(message)
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }
}
