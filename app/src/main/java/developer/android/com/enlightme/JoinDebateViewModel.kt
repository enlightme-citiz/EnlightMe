package developer.android.com.enlightme

import androidx.lifecycle.ViewModel
import developer.android.com.enlightme.objects.Debate
import developer.android.com.enlightme.objects.DebateEntity

class JoinDebateViewModel : ViewModel() {
    // The username
    var userName : String = ""
    // Dict of name network => list of endPointId
    var listNet = mutableMapOf<String, MutableList<String>>()
    //list of devices endpointId to which we are connectec
    val listEndpointId = mutableListOf<String>()
    // My endPointIf seen by the other through Nearby network
    var myEndpointId: String = ""

    fun updateMyEndpointId(endpointId: String, debate: Debate){
        debate.updateEndpointId(myEndpointId, endpointId)
        myEndpointId = endpointId
    }
}