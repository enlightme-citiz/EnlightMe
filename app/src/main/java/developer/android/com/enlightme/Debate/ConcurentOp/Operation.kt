package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
abstract class Operation {

    constructor(debateEntity: DebateEntity) {
        this.debateEntity = debateEntity
    }

    val debateEntity: DebateEntity

    abstract fun perform()
    abstract fun forward(operation: Operation)
    // "this" is the operation that is initially processed before operation. The new "this" and modified operation are the
    // operations that return the same intentions while being switched
    abstract fun backward(operation: Operation): Operation
    // "this" is the operation that is initially processed after operation. The new "this" and modified operation are the
    // operations that return the same intentions while being switched
}