package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.Objects.DebateEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("insert_str")
class InsertStr : Operation {
    constructor(start: Int, strIns: String, target: String) : super() {
        this.start = start
        this.strIns = strIns
        this.target = target
        this.del_befor = mutableListOf<DeleteStr>()
        this.del_after = mutableListOf<DeleteStr>()
    }

    // Insert a character or some characters in the string
    var start: Int // starting position of the string to be deleted
    var strIns: String // String to be inserted
    val target: String // title or description. This targets the string to be changed
    var del_befor: MutableList<DeleteStr>
    var del_after: MutableList<DeleteStr>
    override fun perform(debateEntity: DebateEntity): DebateEntity{
        when (target) {
            "title" -> {
                if (start < debateEntity.title.length){
                    debateEntity.title = debateEntity.title.substring(0,start) + this.strIns +
                            debateEntity.title.substring(start)
                }else{
                    debateEntity.title = debateEntity.title + this.strIns
                }
            }
            "description" -> {
                if (start < debateEntity.description.length){
                    debateEntity.description = debateEntity.description.substring(0,start) + this.strIns +
                            debateEntity.description.substring(start)
                }else{
                    debateEntity.description = debateEntity.description + this.strIns
                }

            }
            else -> {
                throw Exception("Target cannot be confirmed")
            }
        }
        return debateEntity
    }
    companion object{
        fun intersect(listOp1: List<DeleteStr>, listOp2: List<DeleteStr>): Boolean{
            for (op1 in listOp1){
                for (op2 in listOp2){
                    if(op1.compare(op2)){
                        return true
                    }
                }
            }
            return false
        }
    }
    fun priorTo(operation:InsertStr): Int{
        //Test if "this" should be performed prior to operation
        // Return 1 if so, -1 on the contrary. If no order can be obtained (i.e. the strings to be inster are equal),
        // then 0 is returned
        if (this.strIns < operation.strIns){
            return 1
        }
        if (this.strIns > operation.strIns){
            return -1
        }
        if (this.strIns == operation.strIns){
            return 0
        }
        return 0
    }
    override fun forward(operation: Operation){
        if (operation is InsertStr){
            if (operation.start < this.start){
                this.start = start + operation.strIns.length
                return
            }
            if (operation.start == this.start){
                //Check if some deletion has been operated in // of these two.
                // If so determine if the insertion have been perfomed at the
                // same position or not.
                if (intersect(operation.del_after, this.del_befor)){
                    // Current strIns has been performed after operation. But position
                    // are equal because of deletion that has appended in between
                    this.start = start + operation.strIns.length
                    return
                }else{
                    if(!intersect(operation.del_befor, this.del_after)){
                        // Those two insertions have realy been done at the same position.
                        // Dealing with priority rule to ensure that everyone has the same copy
                        if(operation.priorTo(this) == -1){
                            // Deleting history of delete operations
                            this.del_after = mutableListOf()
                            this.del_befor = mutableListOf()
                            return
                        }
                        if(operation.priorTo(this) == 1){
                            // Here the priority rule says that actual operation should be
                            // done after operation
                            this.del_after = mutableListOf()
                            this.del_befor = mutableListOf()
                            this.start = start + operation.strIns.length
                            return
                        }
                        if(operation.priorTo(this) == 0){
                            // Identical operation. We merge it by reducing current operation to
                            // identity
                            this.strIns = ""
                            return
                        }
                    }
                }
            }
        }
        if (operation is DeleteStr){
            if (operation.start < this.start){
                //operation deletion change the position of insertion
                this.start = start - operation.len
                //Keeping record of this deletion for future forward checks
                // between two insertions
                this.del_befor.add(operation)
            }else{
                this.del_after.add(operation)
            }
        }
        return
    }
    override fun backward(operation: Operation): Operation{
        if(operation is InsertStr){
            if(operation.start <= this.start){
                this.start = this.start - operation.strIns.length
                return operation
            }
            if(operation.start > this.start){
                operation.start = operation.start + strIns.length
                return operation
            }
        }
        if(operation is DeleteStr){
            if(operation.start <= this.start){
                this.start = this.start + operation.len
                return operation
            }
            if(operation.start > this.start){
                operation.start = operation.start + strIns.length
                return operation
            }
        }
        return operation
    }
    //TODO vérifier la couverture du code pour les opération forward et backward
}