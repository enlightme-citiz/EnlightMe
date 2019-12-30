package developer.android.com.enlightme

import androidx.lifecycle.ViewModel
import developer.android.com.enlightme.Objects.*
import developer.android.com.enlightme.Utils.PropertyAwareMutableLiveData

class DebateViewModel : ViewModel() {
    //The debate
    val debate = PropertyAwareMutableLiveData<Debate>()
    //Attendee
    val attendee = PropertyAwareMutableLiveData<Attendee>()
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