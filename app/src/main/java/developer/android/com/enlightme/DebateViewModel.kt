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

    fun send_hist_debate(context: Context, list_endpointId:List<String>){
        val json = Json(JsonConfiguration.Stable)
        val pld = Payload.fromBytes(json.stringify(HistDebate.serializer(),hist_debate).toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(list_endpointId, pld)
    }
}