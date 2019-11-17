package developer.android.com.enlightme.objects
import android.content.Context
import developer.android.com.enlightme.Debate.ConcurentOp.Operation
import kotlinx.serialization.*

@Serializable
class Debate {
    var listAttendees = mutableListOf<Attendee>()
    var debateEntity = DebateEntity()
    var current_level = mutableListOf<MutableList<Int>>() // Used to track the current position in the debate. It consiste of a list of tuples(1or2 and Int). To find the current debate, go through the list. At each element the key give the side and the value, the position to go.

    fun get_debate_entity(trace: MutableList<MutableList<Int>>? = null): DebateEntity{
        //TODO un fois que l'outil permettant de naviguer entre les débat sera fait, il faudra mettre à jour current_level

        // Return the debate entity that is currently on screen and edited.
        var level: MutableList<MutableList<Int>> = trace ?: current_level
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
    fun integrate(histEltOp: HistElt, path_to_root: MutableList<MutableList<Int>>){
        // Getting the local debate
        val thisDebate = get_debate_entity(path_to_root)
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
        val thisDebate = get_debate_entity(updatePayload.path_to_root)
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
            if(thisDebate.updateShouldWait(stateVectorOp)){
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
                histEltOp.operation.perform(thisDebate)
            }
        }
    }
    fun manageUserUpdate(listOperation: List<Operation>,
                         context: Context,
                         listEndpointId: List<String>,
                         id_author: String,
                         path_to_root: MutableList<MutableList<Int>>) {
        // Getting the local debate
        val thisDebate = get_debate_entity(path_to_root)
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