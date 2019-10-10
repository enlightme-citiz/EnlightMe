package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
abstract class Operation (debateEntity: DebateEntity){

    val debateEntity: DebateEntity
    init{
        this.debateEntity = debateEntity
    }

    abstract fun perform()
    abstract fun forward(operation: Operation)
    abstract fun backward(operation: Operation): Operation
}