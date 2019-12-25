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

    init {
        debate.value = Debate()
        attendee.value = Attendee()
    }
    // To check wether the viewModel is up to date
    var is_updated = false
}