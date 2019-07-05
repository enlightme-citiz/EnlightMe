package developer.android.com.enlightme

import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.ViewModel
import developer.android.com.enlightme.objects.Attendee
import developer.android.com.enlightme.objects.DebatEntity
import developer.android.com.enlightme.objects.Debate

class DebateViewModel : ViewModel() {
    //The debate
    var debate = Debate()
    //Attendee
    lateinit var attendee: Attendee
    //Debate entity
    var debateEntity = DebatEntity()

    init {
        Log.i("GameViewModel", "GameViewModel created!")
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}