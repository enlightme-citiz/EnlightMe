package developer.android.com.enlightme.Objects
import kotlinx.serialization.Serializable
import developer.android.com.enlightme.Debate.ConcurentOp.Operation
import kotlinx.serialization.Polymorphic

@Serializable
class HistElt {
    constructor(id_author: String, operation: @Polymorphic Operation, state_vector: MutableMap<String, Int>) {
        this.id_author = id_author
        this.operation = operation
        this.state_vector = state_vector
    }

    var id_author: String   //Id of the author of the change. It corresponds to the endpointId
    var operation: Operation
    var state_vector: MutableMap<String, Int>  //Vector that list the number of changes that
}