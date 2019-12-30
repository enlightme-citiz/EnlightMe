package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.Objects.DebateEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("delete_arg")
class DeleteArg : Operation {
    constructor(place: Int, side: Int) : super() {
        this.place = place
        this.side = side
        if ((side != 1) && (side != 2)) {
            throw Exception("Side should be 1 or 2")
        }
    }

    var place: Int
    var side: Int
    override fun perform(debateEntity: DebateEntity): DebateEntity{
        if (this.place>=0){
            if(this.side==1){
                debateEntity.side_1_entity.removeAt(place, debateEntity)
            }else{
                debateEntity.side_2_entity.removeAt(place, debateEntity)
            }
        }
        return debateEntity
    }
    override fun forward(operation: Operation){
        if (operation is InsertArg){
            if ((operation.side == this.side) && (operation.place >= this.place)){
                this.place = place + 1
            }
        }
        if (operation is DeleteArg){
            if ((operation.side == this.side) && (operation.place > this.place)){
                this.place = place - 1
            }
            if ((operation.side == this.side) && (operation.place == this.place)){
                this.place = -1
                // Equivalent to identity operation
            }
        }
    }
    override fun backward(operation: Operation): Operation{
        if(operation is InsertArg){
            if ((operation.side == this.side) && (operation.place < this.place)){
                this.place = this.place - 1
                return operation
            }
            if ((operation.side == this.side) && (operation.place > this.place)){
                operation.place = operation.place - 1
                return operation
            }
            if ((operation.side == this.side) && (operation.place == this.place)){
                this.place = -1
                operation.place = -1
                return operation
            }
        }
        if(operation is DeleteArg){
            if ((operation.side == this.side) && (operation.place <= this.place)){
                this.place = this.place + 1
                return operation
            }
            if ((operation.side == this.side) && (operation.place > this.place)){
                operation.place = operation.place - 1
                return operation
            }
        }
        return operation
    }
    fun compare(op: DeleteArg): Boolean{
        return ((this.place == op.place) && (this.side == op.side))
    }
}