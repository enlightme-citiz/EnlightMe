package developer.android.com.enlightme

import android.util.Log
import androidx.lifecycle.ViewModel
import developer.android.com.enlightme.objects.Attendee
import developer.android.com.enlightme.objects.DebateEntity
import developer.android.com.enlightme.objects.Debate

class DebateViewModel : ViewModel() {
    //The debate
    var debate = Debate()
    //Attendee
    lateinit var attendee: Attendee
    //Debate entity
    var debateEntity = DebateEntity()

    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}