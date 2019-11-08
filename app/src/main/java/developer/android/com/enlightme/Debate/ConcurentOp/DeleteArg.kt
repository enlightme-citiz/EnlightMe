package developer.android.com.enlightme.Debate.ConcurentOp

import developer.android.com.enlightme.objects.DebateEntity
import kotlinx.serialization.Serializable

@Serializable
class DeleteArg : Operation {
    constructor(debateEntity: DebateEntity, place: Int, side: Int) : super(debateEntity) {
        this.place = place
        this.side = side
        if ((side != 1) && (side != 2)) {
            throw Exception("Side should be 1 or 2")
        }
    }

    var place: Int
    var side: Int
    override fun perform(){
        if (this.place>=0){
            if(this.side==1){
                debateEntity.side_1_entity.removeAt(place)
            }else{
                debateEntity.side_2_entity.removeAt(place)
            }
        }
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