package developer.android.com.enlightme.P2PClasses

import android.content.Context
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import developer.android.com.enlightme.DebateViewModel
import developer.android.com.enlightme.objects.Debate
import developer.android.com.enlightme.objects.HistDebate
import developer.android.com.enlightme.objects.UpdatePayload
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


class PayloadByteCallback(viewModel: DebateViewModel, context: Context) : PayloadCallback() {
    lateinit var viewModel: DebateViewModel
    lateinit var context: Context
    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        // This always gets the full data of the payload. Will be null if it's not a BYTES
        // payload. You can check the payload type with payload.getType().
        val receivedBytes = payload.asBytes() ?: ByteArray(0)
        if(receivedBytes.toString() == "Require debate"){
            viewModel.send_update(context,listOf(endpointId))
            return
        }
        // Try to get debate
        val json = Json(JsonConfiguration.Stable)
        val debate = json.parse(Debate.serializer(), String(receivedBytes, Charsets.UTF_8))
        if (viewModel.is_updated == false){
            viewModel.debate?.value?.listAttendees = debate.listAttendees
            viewModel.debate?.value?.debateEntity = debate.debateEntity
        }
        //Try to get updates (Histlist)
        val updatePayload = json.parse(UpdatePayload.serializer(),String(receivedBytes, Charsets.UTF_8))
        viewModel.applyUpdateItem(updatePayload)
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
        // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
        // after the call to onPayloadReceived().
    }

}