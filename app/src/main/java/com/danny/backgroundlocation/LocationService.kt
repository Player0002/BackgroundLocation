package com.danny.backgroundlocation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*

class LocationService : Service() {

    lateinit var locationClient: FusedLocationProviderClient
    private val INTERVAL = 5000L

    lateinit var callback: LocationCallback

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        locationClient = LocationServices.getFusedLocationProviderClient(this)

        callback = object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)
                Toast.makeText(
                    applicationContext,
                    "lat - ${location.lastLocation.latitude}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        locationClient.requestLocationUpdates(
            LocationRequest.create().setInterval(INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setFastestInterval(INTERVAL),
            callback,
            Looper.myLooper()
        )


        val CHANNEL_ID = "Background Location"

        val manager = getSystemService(NotificationManager::class.java) as NotificationManager

        val channel = manager.getNotificationChannel(CHANNEL_ID) ?: NotificationChannel(
            CHANNEL_ID,
            "Background Loc",
            NotificationManager.IMPORTANCE_NONE
        )
        manager.createNotificationChannel(channel)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Background tracking..")
        }
        startForeground(100, builder.build())

    }
}