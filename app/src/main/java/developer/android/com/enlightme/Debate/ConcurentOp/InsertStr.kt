package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
class InsertStr (debateEntity: DebateEntity, start: Int, strIns: String, target: String): Operation(debateEntity){
    // Insert a character or some characters in the string
    var start: Int // starting position of the string to be deleted
    var strIns: String // String to be inserted
    val target: String // title or description. This targets the string to be changed
    init{
        this.start = start
        this.strIns = strIns
        this.target = target
    }
    override fun perform(){
        when (target) {
            "title" -> {
                debateEntity.title = debateEntity.title.substring(0,start) + this.strIns +
                        debateEntity.title.substring(start)
            }
            "description" -> {
                debateEntity.description = debateEntity.description.substring(0,start) + this.strIns +
                        debateEntity.description.substring(start)
            }
            else -> {
                throw Exception("Target cannot be confirmed")
            }
        }
    }
    override fun forward(operation: Operation){
        if (operation is InsertStr){
            if (operation.start >= this.start){
                this.start = start - 1
            }
        }
        if (operation is DeleteStr){
            if (operation.start > this.start){
                this.start = start + 1
            }
            if(operation.start == this.start){
                this.strIns = ""
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
                operation.start = operation.start + 1
                return operation
            }
            if(operation.start == this.start){
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
                operation.start = operation.start + 1
                return operation
            }
            if(operation.start == this.start){
                this.strIns = ""
                operation.start = 0
                operation.len = 0
                return operation
            }
        }
        //TODO deal with other changes type (add argument, delete argument, ...)
        //TODO deal with changes that are not limited to one caracter but a complete string
        throw Exception("Operation should be DeleteStr or InserteStr")
    }
}