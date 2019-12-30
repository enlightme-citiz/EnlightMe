package developer.android.com.enlightme.Objects
import android.content.Context
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import developer.android.com.enlightme.Debate.ConcurentOp.Operation
import developer.android.com.enlightme.Utils.LiveMutableList
import kotlinx.serialization.*

@Serializable
class Debate: BaseObservable(){
    //@Serializer(forClass = Debate::class)
    //companion object : KSerializer<Debate> {
    //    override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Debate") {
    //        init {
    //            addElement("listAttendees") // req will have index 0
    //            addElement("debateEntity") // res will have index 1
    //        }
    //    }
//
    //    override fun serialize(encoder: Encoder, debate: Debate) {
    //        val compositeOutput = encoder.beginStructure(descriptor)
    //        compositeOutput.encodeSerializableElement(descriptor, 0,
    //            ArrayListSerializer(Attendee.serializer()),
    //            debate.listAttendees.getValue() ?: mutableListOf())
    //        compositeOutput.encodeSerializableElement(descriptor, 1,
    //            DebateEntity.serializer(),
    //            debate.debateEntity)
    //        compositeOutput.endStructure(descriptor)
    //    }
//
    //    override fun deserialize(decoder: Decoder): Debate {
    //    }
    //}
    @Bindable
    var listAttendees = LiveMutableList<Attendee>()
        set(value) {
            field = value
            notifyPropertyChanged(BR.listAttendees)
        }
    @Bindable
    var debateEntity = DebateEntity()
        set(value) {
            field = value
            notifyPropertyChanged(BR.debateEntity)
        }
    @Bindable
    var currentLevel = LiveMutableList<LiveMutableList<Int>>() // Used to track the current position in
        // the debate. It consiste of a list of tuples(1or2 and Int). To find the current debate,
        // go through the list. At each element the key give the side and the value, the position
        // to go.
        set(value) {
            field = value
            notifyPropertyChanged(BR.currentLevel)
        }

    fun getDebateEntity(trace: LiveMutableList<LiveMutableList<Int>>? = null): DebateEntity{
        //TODO un fois que l'outil permettant de naviguer entre les débat sera fait, il faudra.
        // mettre à jour current_level

        // Return the debate entity that is currently on screen and edited.
        var level: LiveMutableList<LiveMutableList<Int>> = trace ?: currentLevel
        var curr_debate = debateEntity
        for(tup in level){
            if(tup[0] == 1){
                curr_debate = curr_debate.side_1_entity[tup[1]]
            }else{
                curr_debate = curr_debate.side_2_entity[tup[1]]
            }
        }
        return curr_debate
    }

    fun setDebateEntity(trace: LiveMutableList<LiveMutableList<Int>>? = null, newDebateEntity: DebateEntity){
        //TODO un fois que l'outil permettant de naviguer entre les débat sera fait, il faudra.
        // mettre à jour current_level

        //TODO mettre à jour le debateEntity. Trouver un moyen de créer des pointeurs au lieu des corrections.
        // Set the new debate entity after change.
        var level: LiveMutableList<LiveMutableList<Int>> = trace ?: currentLevel

        Log.i("Debate", "trace")
        Log.i("Debate", level.size.toString())

        Log.i("Debate", "debateEntity Size")
        Log.i("Debate", debateEntity.side_1_entity.size.toString())
        Log.i("Debate", debateEntity.side_2_entity.size.toString())

        Log.i("Debate", "newDebateEntity Size")
        Log.i("Debate", debateEntity.side_1_entity.size.toString())
        Log.i("Debate", debateEntity.side_2_entity.size.toString())

        if(level.size > 0){
            if(level[0][0] == 1){
                debateEntity.side_1_entity[level[0][1]].setSubDebateEntity(level, newDebateEntity)
            }else{
                debateEntity.side_2_entity[level[0][1]].setSubDebateEntity(level, newDebateEntity)
            }
        }else{
            debateEntity = newDebateEntity
        }

        Log.i("Debate", "debateEntity Size")
        Log.i("Debate", debateEntity.side_1_entity.size.toString())
        Log.i("Debate", debateEntity.side_2_entity.size.toString())
    }

    fun integrate(histEltOp: HistElt, path_to_root: LiveMutableList<LiveMutableList<Int>>){
        // Getting the local debate
        val thisDebate = getDebateEntity(path_to_root)
        //TODO check for loop boundaries
        val n1 = thisDebate.separateHist(histEltOp)
        for (i in n1..thisDebate.hist_debate.histEltList.size-1){
            histEltOp.operation.forward(thisDebate.hist_debate.histEltList[i].operation)
        }
        histEltOp.operation.perform(thisDebate)
        thisDebate.hist_debate.histEltList.add(histEltOp)
        if (thisDebate.state_vector[histEltOp.id_author] != null){
            thisDebate.state_vector[histEltOp.id_author] = thisDebate.state_vector[histEltOp.id_author]!! + 1
        }else{
            thisDebate.state_vector[histEltOp.id_author] = 1
        }
    }

    fun updateEndpointId(oldIdAuthor: String, newIdAuthor: String){
        if (oldIdAuthor != newIdAuthor){
            //update the endpointId in histEltList of each debate_entities.
            val pile = mutableListOf(debateEntity)
            while (pile.size >0){
                val debEnt = pile.removeAt(0)
                for (debEntS1 in debEnt.side_1_entity){
                    pile.plus(debEntS1)
                }
                for (debEntS2 in debEnt.side_2_entity){
                    pile.plus(debEntS2)
                }
                for(hs in debEnt.hist_debate.histEltList){
                    if (hs.id_author == oldIdAuthor)
                        hs.id_author = newIdAuthor
                }
            }
        }
    }

    fun manageOthersUpdate(updatePayload: UpdatePayload,
                           id_author: String){
        // Getting the local debate
        val thisDebate = getDebateEntity(updatePayload.path_to_root)
        // Check if no preceding operation are missing
        val histEltOp: HistElt = updatePayload.hist_elt
        val stateVectorOp: MutableMap<String, Int> = histEltOp.state_vector
        var nbChangeToApply = thisDebate.updateWaitingList.size+1
        if(thisDebate.updateShouldWait(stateVectorOp)){
            thisDebate.updateWaitingList = thisDebate.updateWaitingList.plus(updatePayload)
            nbChangeToApply -= 1 // To not try to apply this update that should still wait for a new update.
        }
        if (nbChangeToApply == thisDebate.updateWaitingList.size+1){
            // No update are missing. updatePayload can be treated first in the list of updateWaitingList
            thisDebate.updateWaitingList = listOf(updatePayload) + thisDebate.updateWaitingList
        }
        for (i in 0..nbChangeToApply){
            if(!thisDebate.updateShouldWait(stateVectorOp)){
                // For each update in waiting list do
                // Integrate in the history
                integrate(histEltOp, thisDebate.path_to_root)
                // Update state_vector
                if (!(id_author in thisDebate.state_vector)){
                    thisDebate.state_vector[id_author] = 1
                }else{
                    thisDebate.state_vector[id_author] = thisDebate.state_vector[id_author]!! + 1
                }
                // Apply changes
                Log.i("Debate", "perform")
                val newDebateEntity = histEltOp.operation.perform(thisDebate)
                setDebateEntity(updatePayload.path_to_root, newDebateEntity)
            }
        }
    }
    fun manageUserUpdate(listOperation: List<Operation>,
                         context: Context,
                         listEndpointId: List<String>,
                         id_author: String,
                         path_to_root: LiveMutableList<LiveMutableList<Int>>) {
        // Getting the local debate
        val thisDebate = getDebateEntity(path_to_root)
        //Create HistElt
        for (lo in listOperation) {
            val histElt = HistElt(id_author, lo, thisDebate.state_vector)
            //Broadcast it
            thisDebate.send_update(context, listEndpointId, histElt)
            //manageOthersUpdate since the broadcast will not reach us (see Nearby doc)
            val updatePayload = UpdatePayload(histElt, path_to_root)
            manageOthersUpdate(updatePayload, id_author)
        }
    }
}