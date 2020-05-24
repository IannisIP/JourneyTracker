package journey.project.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.gms.location.*
import journey.project.activities.MainActivity
import journey.project.R

class ServiciuLocalizare : Service() {
    val interval: Long = 10000 //10 sec
    val fastestInterval: Long = 2000 //5 sec
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    val locationRequest = LocationRequest()

    //prin intermediul acestui obiect se preiau coordonatele
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val loc = locationResult?.lastLocation
            val intent = Intent("ACTION_COORD")
            //TODO: de analizat frecventa de trasmitere a mesajelor si posbilitati de optmizare
            intent.putExtra("COORD", "${loc?.latitude}, ${loc?.longitude}")
            sendBroadcast(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //oprim automat serviciul daca nu avem permisiunea ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            startLocalizare()
            //se poate afisa si o notificare pentru a arata ca serviciul ruleaza
            //Notification si NotificationManager
        } else
            stopSelf()
    }

    //se apeleaza pentru fiecare invocare startService

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //24-04-2020 - in onStartCommand putem prelua parametrii trasnmisi prin intent

        creeazaSiAfiseazaNotificarea()

        return super.onStartCommand(intent, flags, startId)

    }
    //24.04.2020 - afisare notificare la pornire serviciu
    fun creeazaSiAfiseazaNotificarea() {

        val notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //pentru Android O
        val channelId = "serv_loc"

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Canal serviciu localizare", NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel)
        }
        // include argumentele pe care le va primi fragmentul traseuNouFragment, pentru a restaura starea constroalelor
        val bundle = Bundle()
        //TODO: de populat obiectul de tip Bundle in vederea restaurarii fragmentului TraseuNou

        //utilizat la intereactiunea cu notificarea -> se va deschide fragmentul TraseuNou
        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.newTravelFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, "serv_loc")
            .setSmallIcon(R.drawable.ic_add_white_24dp)//TODO: de schimbat pictograma
            .setContentTitle(getString(R.string.app_title))
            .setContentText("Descriere...")
            .setContentIntent(pendingIntent)
            .build();

        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocalizare()
        //24.04.2020 - eliminam notificarea
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
