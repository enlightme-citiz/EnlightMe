package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
class InsertArg : Operation {
    constructor(
        subDebateEntity: DebateEntity,
        place: Int,
        side: Int
    ) : super() {
        this.subDebateEntity = subDebateEntity
        this.place = place
        this.side = side
        this.del_befor = listOf()
        this.del_after = listOf()
        if ((side != 1) && (side != 2)) {
            throw Exception("Side should be 1 or 2")
        }
    }

    val subDebateEntity: DebateEntity
    var place: Int
    var side: Int
    var del_befor: List<DeleteArg>
    var del_after: List<DeleteArg>
    override fun perform(debateEntity: DebateEntity){
        if (this.place>=0){
            if(this.side==1){
                debateEntity.side_1_entity.add(place,this.subDebateEntity)
            }else{
                debateEntity.side_2_entity.add(place,this.subDebateEntity)
            }
        }
    }

    companion object{
        fun intersect(listOp1: List<DeleteArg>, listOp2: List<DeleteArg>): Boolean{
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
    fun priorTo(operation:InsertArg): Int{
        //Test if "this" should be performed prior to operation
        // Return 1 if so, -1 on the contrary. If no order can be obtained (i.e. the strings to be inster are equal),
        // then 0 is returned
        if (this.subDebateEntity.title < operation.subDebateEntity.title){
            return 1
        }
        if (this.subDebateEntity.title > operation.subDebateEntity.title){
            return -1
        }
        if (this.subDebateEntity.title == operation.subDebateEntity.title){
            return 0
        }
        return 0
    }
    override fun forward(operation: Operation){
        if (operation is InsertArg){
            if (operation.side == this.side){
                if (operation.place > this.place){
                    this.place = place + 1
                }
                if (operation.place == this.place){
                    //Check if some deletion has been operated in // of these two.
                    // If so determine if the insertion have been perfomed at the
                    // same position or not.
                    if (intersect(operation.del_after, this.del_befor)){
                        // Current strIns has been performed after operation. But position
                        // are equal because of deletion that has appended in between
                        this.place = place + 1
                    }else{
                        if(!intersect(operation.del_befor, this.del_after)){
                            // Those two insertions have realy been done at the same position.
                            // Dealing with priority rule to ensure that everyone has the same copy
                            if(this.priorTo(operation) == 1){
                                // Deleting history of delete operations
                                this.del_after = listOf()
                                this.del_befor = listOf()
                            }
                            if(operation.priorTo(this) == 1){
                                // Here the priority rule says that actual operation should be
                                // done after operation
                                this.del_after = listOf()
                                this.del_befor = listOf()
                                this.place = place + 1
                            }
                            if(operation.priorTo(this) == 0){
                                // Identical operation. We merge it by reducing current operation to
                                // identity
                                this.place = -1
                            }
                        }
                    }
                }
            }
        }
        if (operation is DeleteArg){
            if (operation.side == this.side) {
                if (operation.place < this.place) {
                    //operation deletion change the position of insertion
                    this.place = place - 1
                    //Keeping record of this deletion for future forward checks
                    // between two insertions
                    this.del_befor.plus(operation)
                } else {
                    this.del_after.plus(operation)
                }
            }
        }
    }
    override fun backward(operation: Operation): Operation{
        if(operation is InsertArg){
            if ((operation.side == this.side) && (operation.place <= this.place)){
                this.place = this.place - 1
                return operation
            }
            if((operation.side == this.side) && (operation.place > this.place)){
                operation.place = operation.place + 1
                return operation
            }
        }
        if(operation is DeleteArg){
            if((operation.side == this.side) && (operation.place < this.place)){
                this.place = this.place + 1
                return operation
            }
            if((operation.side == this.side) && (operation.place > this.place)){
                operation.place = operation.place + 1
                return operation
            }
            if((operation.side == this.side) && (operation.place == this.place)){
                this.place = -1
                operation.place = -1
                return operation
            }
        }
        return operation
    }
}