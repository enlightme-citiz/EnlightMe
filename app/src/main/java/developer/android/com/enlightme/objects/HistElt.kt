package developer.android.com.enlightme.objects
import kotlinx.serialization.Serializable
import developer.android.com.enlightme.Debate.ConcurentOp.Operation
@Serializable
class HistElt (id_author: String, operation: Operation, state_vector: MutableMap<String, Int>){
    var id_author: String  //Id of the author of the change. It corresponds to the endpointId
    var operation: Operation
    var state_vector = mutableMapOf<String, Int>() //Vector that list the number of changes that
    init{
        this.operation = operation
        this.id_author = id_author
        this.state_vector = state_vector
    }
}