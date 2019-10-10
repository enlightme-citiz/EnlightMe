package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
class DeleteStr (debateEntity: DebateEntity, start: Int, len: Int, target: String): Operation(debateEntity){
    // Delete a character or some character in the string
    var start: Int // starting position of the string to be deleted if start == -1, the operation is equivalent to the identity
    var len: Int   // length of the string to be deleted
    val target: String // title or description. This targets the string to be changed
    init{
        this.start = start
        this.len = len
        this.target = target
        if (len>1){
            throw Exception("String modification are not supported yet.") //TODO deal with string instead of simple char if necessary
        }
    }
    override fun perform(){
        when (target) {
            "title" -> {
                debateEntity.title = debateEntity.title.substring(0,start) +
                        debateEntity.title.substring(start+len)
            }
            "description" -> {
                debateEntity.description = debateEntity.description.substring(0,start) +
                        debateEntity.description.substring(start+len)
            }
            else -> {
                throw Exception("Target cannot be confirmed")
            }
        }
    }
    override fun forward(operation: Operation){
        if (operation is InsertStr){
            if (operation.start >= this.start){
                this.start = start + 1
            }
        }
        if (operation is DeleteStr){
            if (operation.start > this.start){
                this.start = start - 1
            }
            if(operation.start == this.start){
                this.start = 0
                this.len = 0
                // Equivalent to identity operation
            }
        }
        //TODO deal with other changes type (add argument, delete argument, ...)
        //TODO deal with changes that are not limited to one caracter but a complete string
    }
    override fun backward(operation: Operation): Operation{
        if(operation is InsertStr){
            if(operation.start < this.start){
                this.start = this.start - 1
                return operation
            }
            if(operation.start > this.start){
                operation.start = operation.start - 1
                return operation
            }
            if(operation.start == this.start){
                this.start = 0
                this.len = 0
                operation.strIns = ""
                return operation
            }
        }
        if(operation is DeleteStr){
            if(operation.start < this.start){
                this.start = this.start + 1
                return operation
            }
            if(operation.start > this.start){
                operation.start = operation.start - 1
                return operation
            }
            if(operation.start == this.start){
                this.start = 0
                this.len = 0
                return operation
            }
        }
        //TODO deal with other changes type (add argument, delete argument, ...)
        //TODO deal with changes that are not limited to one caracter but a complete string
        throw Exception("Operation should be DeleteStr or InserteStr")
    }
}