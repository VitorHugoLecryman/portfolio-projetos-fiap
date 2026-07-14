package br.com.fiap.biomonitor.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val timestamp: Long = System.currentTimeMillis()
)

sealed class LocationOutcome {
    data class Success(val location: LocationData) : LocationOutcome()
    data class Error(val message: String) : LocationOutcome()
    object PermissionDenied : LocationOutcome()
    object LocationDisabled : LocationOutcome()
}

@Singleton
class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }


    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationOutcome {
        if (!hasLocationPermission()) {
            return LocationOutcome.PermissionDenied
        }

        return try {
            val location = suspendCancellableCoroutine<Location?> { continuation ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }

            if (location != null) {
                LocationOutcome.Success(
                    LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracy = location.accuracy,
                        timestamp = location.time
                    )
                )
            } else {

                getFreshLocation()
            }
        } catch (e: Exception) {
            LocationOutcome.Error(e.message ?: ErrorMessages.LOCATION_UNAVAILABLE)
        }
    }


    @SuppressLint("MissingPermission")
    private suspend fun getFreshLocation(): LocationOutcome {
        if (!hasLocationPermission()) {
            return LocationOutcome.PermissionDenied
        }

        return try {
            val location = suspendCancellableCoroutine<Location> { continuation ->
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    1000L
                ).apply {
                    setMaxUpdates(1)
                    setWaitForAccurateLocation(false)
                }.build()

                val callback = object : LocationCallback() {
                    override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                        result.lastLocation?.let { location ->
                            fusedLocationClient.removeLocationUpdates(this)
                            continuation.resume(location)
                        }
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    callback,
                    Looper.getMainLooper()
                )

                continuation.invokeOnCancellation {
                    fusedLocationClient.removeLocationUpdates(callback)
                }
            }

            LocationOutcome.Success(
                LocationData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    accuracy = location.accuracy,
                    timestamp = location.time
                )
            )
        } catch (e: Exception) {
            LocationOutcome.Error(e.message ?: ErrorMessages.LOCATION_UNAVAILABLE)
        }
    }


    @SuppressLint("MissingPermission")
    fun observeLocation(intervalMs: Long = 10000L): Flow<LocationData> = callbackFlow {
        if (!hasLocationPermission()) {
            close(SecurityException(ErrorMessages.LOCATION_PERMISSION_DENIED))
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervalMs
        ).apply {
            setMinUpdateIntervalMillis(intervalMs / 2)
        }.build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(
                        LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracy = location.accuracy,
                            timestamp = location.time
                        )
                    )
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }


    fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }
}
