package developer.android.com.enlightme

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload
import developer.android.com.enlightme.objects.*
import kotlinx.serialization.json.*

class DebateViewModel : ViewModel() {
    //The debate
    val debate = MutableLiveData<Debate>()
    //Attendee
    val attendee = MutableLiveData<Attendee>()
    //Temporary debate entity. Enable instantiating and communicating debateEntity between
    // alertDialogue and debatFragment when creating a new argument
    var temp_debate_entity = DebateEntity()
    var temp_side = 0
    var edit_arg_pos = -1
    // History of changes brought to the debate by each attendee. This history is shared between attendees to deal with
    // concurent editing.
    var hist_debate = HistDebate()
    init {
        debate.value = Debate()
        attendee.value = Attendee()
    }
    // To check wether the viewModel is up to date
    var is_updated = false

    fun send_update(context: Context, list_endpointId:List<String>){
        val json = Json(JsonConfiguration.Stable)
        // TODO Intitialize val state_vector
        val update_payload = UpdatePayload(hist_debate.hist_debate[hist_debate.hist_debate.size-1], state_vector)
        val pld = Payload.fromBytes(json.stringify(UpdatePayload.serializer(),
            update_payload).toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(list_endpointId, pld)
    }
    fun applyUpdateItem(updatePayload: UpdatePayload){
        val hist_elt = updatePayload.hist_elt
        val state_vector = updatePayload.state_vector
        //TODO apply update
        // Check that the number of change made by the author of hist_elt is >
    }
}