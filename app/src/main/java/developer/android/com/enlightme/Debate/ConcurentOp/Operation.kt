package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("operation")
abstract class Operation {
    abstract fun perform(debateEntity: DebateEntity)
    abstract fun forward(operation: Operation)
    // "this" is the operation that is initially processed before operation. The new "this" and modified operation are the
    // operations that return the same intentions while being switched
    abstract fun backward(operation: Operation): Operation
    // "this" is the operation that is initially processed after operation. The new "this" and modified operation are the
    // operations that return the same intentions while being switched

}