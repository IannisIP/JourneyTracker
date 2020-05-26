package journey.project.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.location.*
import journey.project.activities.MainActivity
import journey.project.R
import java.util.*

class ServiciuLocalizare : Service() {
    val interval: Long = 10000
    val fastestInterval: Long = 2000
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    val locationRequest = LocationRequest()
    lateinit var context: Context

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val loc = locationResult?.lastLocation

            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(loc?.latitude!!, loc.longitude, 1)

            val intent = Intent("ACTION_COORD")
            intent.putExtra("COORD", "${loc.latitude}, ${loc.longitude}")

            if(address != null && address.isNotEmpty()) {
                intent.putExtra("Country", "${address[0].countryName}")
                intent.putExtra("Locality", "${address[0].locality}")
                intent.putExtra("Address", "${address[0].thoroughfare}")
            }

            sendBroadcast(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            context = this

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            startLocalizare()

        } else
            stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        creeazaSiAfiseazaNotificarea()

        return super.onStartCommand(intent, flags, startId)

    }
    fun creeazaSiAfiseazaNotificarea() {

        val notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "serv_loc"

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Canal serviciu localizare", NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel)
        }
        val bundle = Bundle()
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.newTravelFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, "serv_loc")
            .setSmallIcon(R.drawable.ic_add_white_24dp)
            .setContentTitle(getString(R.string.app_title))
            .setContentText("Descriere...")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocalizare()
        stopForeground(true)
    }

    fun startLocalizare() {
        locationRequest.interval = interval
        locationRequest.fastestInterval = fastestInterval
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun stopLocalizare() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }
}
