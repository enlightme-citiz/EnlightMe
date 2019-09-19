package developer.android.com.enlightme.objects

import kotlinx.serialization.Serializable

@Serializable
class UpdatePayload(hist_elt: HistElt, state_vector: MutableMap<String, Int>){
    lateinit var hist_elt: HistElt
    lateinit var state_vector: MutableMap<String, Int>
}
// Object that is send to the network and used to carry update informations (operation and state vector)