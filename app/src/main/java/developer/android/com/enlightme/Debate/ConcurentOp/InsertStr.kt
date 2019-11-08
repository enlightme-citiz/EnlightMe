package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
class InsertStr : Operation {
    constructor(debateEntity: DebateEntity, start: Int, strIns: String, target: String) : super(debateEntity) {
        this.start = start
        this.strIns = strIns
        this.target = target
        this.del_befor = listOf()
        this.del_after = listOf()
    }

    // Insert a character or some characters in the string
    var start: Int // starting position of the string to be deleted
    var strIns: String // String to be inserted
    val target: String // title or description. This targets the string to be changed
    var del_befor: List<DeleteStr>
    var del_after: List<DeleteStr>
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
            }
            if (operation.start == this.start){
                //Check if some deletion has been operated in // of these two.
                // If so determine if the insertion have been perfomed at the
                // same position or not.
                if (intersect(operation.del_after, this.del_befor)){
                    // Current strIns has been performed after operation. But position
                    // are equal because of deletion that has appended in between
                    this.start = start + operation.strIns.length
                }else{
                    if(!intersect(operation.del_befor, this.del_after)){
                        // Those two insertions have realy been done at the same position.
                        // Dealing with priority rule to ensure that everyone has the same copy
                        if(operation.priorTo(this) == -1){
                            // Deleting history of delete operations
                            this.del_after = listOf()
                            this.del_befor = listOf()
                        }
                        if(operation.priorTo(this) == 1){
                            // Here the priority rule says that actual operation should be
                            // done after operation
                            this.del_after = listOf()
                            this.del_befor = listOf()
                            this.start = start + operation.strIns.length
                        }
                        if(operation.priorTo(this) == 0){
                            // Identical operation. We merge it by reducing current operation to
                            // identity
                            this.strIns = ""
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
                this.del_befor.plus(operation)
            }else{
                this.del_after.plus(operation)
            }
        }
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
            if(operation.start < this.start){
                this.start = this.start + operation.len
                return operation
            }
            if(operation.start > this.start){
                operation.start = operation.start + strIns.length
                return operation
            }
            if(operation.start == this.start){
                this.strIns = ""
                operation.start = 0
                operation.len = 0
                return operation
            }
        }
        return operation
    }
}