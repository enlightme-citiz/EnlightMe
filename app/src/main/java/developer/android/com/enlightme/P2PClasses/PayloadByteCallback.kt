package developer.android.com.enlightme.P2PClasses

import android.content.Context
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import developer.android.com.enlightme.DebateViewModel
import developer.android.com.enlightme.JoinDebateViewModel
import developer.android.com.enlightme.objects.Debate
import developer.android.com.enlightme.objects.HistDebate
import developer.android.com.enlightme.objects.UpdatePayload
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


class PayloadByteCallback() : PayloadCallback() {
    lateinit var debateViewModel: DebateViewModel
    lateinit var joinDebateViewModel: JoinDebateViewModel
    lateinit var context: Context
    constructor(debateViewModel: DebateViewModel, joinDebateViewModel: JoinDebateViewModel, context: Context) : this(){
        this.debateViewModel = debateViewModel
        this.context = context
        this.joinDebateViewModel = joinDebateViewModel
    }

    override fun onPayloadReceived(endpointId: String, payload: Payload) {
        // This always gets the full data of the payload. Will be null if it's not a BYTES
        // payload. You can check the payload type with payload.getType().
        val receivedBytes = payload.asBytes() ?: ByteArray(0)
        val payloadAsStr = String(receivedBytes, Charsets.UTF_8)
        if((payloadAsStr.length == 5)&&("[a-zA-Z0-9]+".toRegex().matches(payloadAsStr))){ // Someone just connect to
            // us and requires the debate.
            // The message send is just our endpointId (that he only sees). We use it to update our history if this is
            // the first time we connect to someone (and thus we don't know our endpointId.
            // We send him his endpoint and the debate then
            joinDebateViewModel.updateMyEndpointId(String(receivedBytes, Charsets.UTF_8),
                debateViewModel.debate.value ?: Debate())
            debateViewModel.debate.value?.debateEntity?.send_whole_debate(context,endpointId)
            return
        }
        // Try to get debate
        val json = Json(JsonConfiguration.Stable)
        val debate = json.parse(Debate.serializer(), String(receivedBytes, Charsets.UTF_8))
        if (debateViewModel.is_updated == false){
            debateViewModel.debate?.value?.listAttendees = debate.listAttendees
            debateViewModel.debate?.value?.debateEntity = debate.debateEntity
        }
        //Try to get updates (of type updatePayload)
        val updatePayload = json.parse(UpdatePayload.serializer(),String(receivedBytes, Charsets.UTF_8))
        debateViewModel.debate.value?.get_debate_entity()?.manageOthersUpdate(updatePayload, endpointId)
    }

    override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
        // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
        // after the call to onPayloadReceived().
    }
}