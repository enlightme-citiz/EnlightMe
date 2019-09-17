package developer.android.com.enlightme.objects
import kotlinx.serialization.Serializable
import kotlin.reflect.KParameter
@Serializable
class HistElt {
    var id_author = "" //Id of the author of the change. It corresponds to the endpointId
    var number_of_change = 0 //number of change made by the author before this one
    var func = object {} //function of this change
    var arg_func = mutableMapOf<KParameter, Any?>() // parameters of this change
}