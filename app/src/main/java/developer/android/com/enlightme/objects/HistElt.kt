package developer.android.com.enlightme.objects
import kotlinx.serialization.Serializable
import developer.android.com.enlightme.Debate.ConcurentOp.Operation
@Serializable
class HistElt (id_author: String, operation: Operation){
    var id_author: String  //Id of the author of the change. It corresponds to the endpointId
    var operation: Operation
    init{
        this.operation = operation
        this.id_author = id_author
    }
}