package developer.android.com.enlightme.P2PClasses

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*


class P2P(context: Context, endpointDiscoveryCallback : EndpointDiscoveryCallback){

    val service_id = context.packageName ?: "developer.android.com.enlightme"
    val context = context
    val endpointDiscoveryCallback = endpointDiscoveryCallback
    val connectionsClient = Nearby.getConnectionsClient(context)
    val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
            // Automatically accept the connection on both sides.
            // connectionsClient.acceptConnection(endpointId, PayloadByteCallback())
        }
        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            when (result.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                }
            }// We're connected! Can now start sending and receiving data.
            // The connection was rejected by one or both sides.
            // The connection broke before it was able to be accepted.
            // Unknown status code
        }
        override fun onDisconnected(endpointId: String) {
            // We've been disconnected from this endpoint. No more data can be
            // sent or received.
        }
    }
    fun startAdvertising(networkName : String) {
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        val advertiser = connectionsClient.startAdvertising(networkName, service_id, connectionLifecycleCallback,
            advertisingOptions)
        advertiser.addOnSuccessListener {
                val text = "Réseau créer sous le nom $networkName"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        advertiser.addOnFailureListener { e: Exception ->
                val text = "Impossible de créer le réseau. Message d'erreur ${e.message}"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
    }
    fun listAvailablePeers() {
        connectionsClient.startDiscovery(service_id, endpointDiscoveryCallback,
            DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build())
    }

}