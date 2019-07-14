package developer.android.com.enlightme

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import developer.android.com.enlightme.objects.Attendee
import developer.android.com.enlightme.objects.DebateEntity
import developer.android.com.enlightme.objects.Debate

class DebateViewModel : ViewModel() {
    //The debate
    val debate = MutableLiveData<Debate>()
    //Attendee
    val attendee = MutableLiveData<Attendee>()
    //Temporary debate entity. Enable instantiating and communicating debateEntity between
    // alertDialogue and debatFragment when creating a new argument
    var temp_debate_entity = DebateEntity()
    var temp_side = 0

    init {
        Log.i("DebateViewModel", "DebateViewModel created!")
        debate.value = Debate()
        attendee.value = Attendee()
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("DebateViewModel", "DebateViewModel destroyed!")
    }
}