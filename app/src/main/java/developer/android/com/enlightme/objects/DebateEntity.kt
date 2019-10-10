package developer.android.com.enlightme.objects

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload
import developer.android.com.enlightme.Debate.ConcurentOp.Operation
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.serializer
import java.util.*

@Serializable
class DebateEntity {
    var path_to_root = mutableListOf<MutableList<Int>>() // gives the path from the root to
    // reach this argument
    var title = ""
    var description = ""
    var side_1 = "Side 1"
    var side_2 = "Side 2"
    var side_1_entity = mutableListOf<DebateEntity>()
    var side_2_entity  = mutableListOf<DebateEntity>()
    // History of changes brought to the debate by each attendee. This history is shared between attendees to deal with
    // concurent editing.
    var hist_debate = HistDebate()
    var state_vector = mutableMapOf<String, Int>() //Vector that list the number of changes that
    // each attendee performed. See doc:
    // State Vector: Let N be the number of sites in the collaborative system. These sites are numbered from 1 to N.
    // On every site S, and for every object O, a vector Vs,o composed of N elements is defined where: Vs,o[j] =
    // number of operations generated by site j, received by the site S and executed by S on its copy of the object O.

    var updateWaitingList = listOf<UpdatePayload>()

    //Modification and update procedure are coded here. Two things should be considered:
        //1. History management
        //2. modifications brought to the debatEntity object
    // Each time the history management should be performed first and then changes should be applied
    // First case: An update arrive from outside
        // Check for concurency and change history if necessary to integrate the operation in the history
        // Apply the change
    // Second case: The user make a change and broadcast it.
        // Create an update element
        // Broadcaast it
        // Manage the update as if it where an update coming from someone else (case 1)
    // Function needed:
        //For History management
            // Separate history
            // Backward transpose
            // Integrate in the history
        //Network
            // Broadcast the update
        //Update management
            // Deal with a change from the user
            // Deal with a change coming from outside
    fun send_update(context: Context, listEndpointId:List<String>, histElt: HistElt){
        val json = Json(JsonConfiguration.Stable)
        val update_payload = UpdatePayload(histElt, state_vector, path_to_root)
        val pld = Payload.fromBytes(json.stringify(UpdatePayload.serializer(),
            update_payload).toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(listEndpointId, pld)
    }

    fun integrate(histEltOp: HistElt, stateVectorOp: MutableMap<String, Int>){
        //TODO implement seperateHist and backwardtranpose and inegrate
    }
        // Separate history
        separateHist(histEltOp, stateVectorOp)
        // Backward transpose
        backwardTranspose(histEltOp, stateVectorOp)
    }
    fun manageOthersUpdate(updatePayload: UpdatePayload,
                           id_author: String){
        // Check if no preceding operation are missing
        val histEltOp: HistElt = updatePayload.hist_elt
        val stateVectorOp: MutableMap<String, Int> = updatePayload.state_vector
        var nbChangeToApply = updateWaitingList.size+1
        for ((svOpK, svOp) in stateVectorOp){
            if ((state_vector[svOpK] ?: svOp-1) < svOp){
                updateWaitingList = updateWaitingList.plus(updatePayload)
                nbChangeToApply -= 1
                break
            }
        }
        if (nbChangeToApply == updateWaitingList.size+1){
            // No update are missing. updatePayload can be treated first in the list of updateWaitingList
            updateWaitingList = listOf(updatePayload) + updateWaitingList
        }
        for (i in 0..nbChangeToApply){
            // For each update in waiting list do
            // Integrate in the history
            integrate(histEltOp, stateVectorOp)
            // Update state_vector
            if (!(id_author in state_vector)){
                state_vector[id_author] = 1
            }else{
                state_vector[id_author] = state_vector[id_author]!! + 1
            }
            // Apply changes
            histEltOp.operation.perform()
        }
    }
    fun manageUserUpdate(listOperation: List<Operation>,
                         context: Context,
                         listEndpointId: List<String>,
                         id_author: String){
        //Create HistElt
        for (lo in listOperation){
            val histElt = HistElt(id_author, lo)
        //Broadcast it
            this.send_update(context, listEndpointId, histElt)
        //manageOthersUpdate since the broadcast will not reach us (see Nearby doc)
            val updatePayload = UpdatePayload(histElt, state_vector, path_to_root)
            manageOthersUpdate(updatePayload, id_author)
    }


    // function to be called when one want to apply modification and send update to the others
    fun update_and_mod(listOperation: List<Operation>,
                       context: Context, listEndpointId: List<String>,
                       id_author: String){
        var number_of_change = hist_debate.getNbChange(id_author)
        for (lo in listOperation){
            val histElt = HistElt(id_author, number_of_change, lo)
            send_update(context, listEndpointId, histElt)
            // TODO apply applyUpdateItem here then apply new transformation functions and then update histElt
            // hist_debate.histEltList.plus(histElt)
            // func(args, this)
            if (!(id_author in state_vector)){
                state_vector[id_author] = 1
            }else{
                state_vector[id_author] = state_vector[id_author]!!.plus(1)
            }
        }
    }
    fun applyUpdateItem(updatePayload: UpdatePayload, id_author: String){
        //TODO vérifier que updateWaitingList est vide. Sinon mettre l'opération courante en tête de file et tenter
        // d'appliquer l'ensemble des update dans la file d'attente
        val histEltOp: HistElt = updatePayload.hist_elt
        val stateVectorOp: MutableMap<String, Int> = updatePayload.state_vector
        var applyUpdate = true
        for ((svOpK, svOp) in stateVectorOp){
            if ((state_vector[svOpK] ?: svOp-1) < svOp){
                updateWaitingList.plus(listOf(histEltOp, stateVectorOp))
                applyUpdate = false
                break
            }
        }
        // check for concurency
        val concurence = mutableMapOf<String, Int>() // collect all node that display conflict
        if (applyUpdate){
            //TODO remplacer func et args par l'objet de type Operation
            histEltOp.func(histEltOp.arg_func, this)
            hist_debate.histEltList.plus(histEltOp)
            if (!(id_author in state_vector)){
                state_vector[id_author] = 1
            }else{
                state_vector[id_author] = state_vector[id_author]!!.plus(1)
            }
        }
        //TODO gérer les concurences...
        // puis renvoyer les arg et les fonction modiéfiées
    }
    fun send_whole_debate(context: Context, endpointId: String){
        val json = Json(JsonConfiguration.Stable)
        var pld = Payload.fromBytes(json.stringify(serializer(),
            this).toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(endpointId, pld)
        pld = Payload.fromBytes(endpointId.toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(endpointId, pld)
    }

    companion object {
        //Function that process function if they are concurent

        // Every necerassary function to change the debate are stored here. These function does not change the appearance
        // on UI but just the data. It enables dealing with the history and concurent editing
        //TODO mettre toutes ces fonction dans les classes Operation
        fun addArgument(args: List<Any>, thisDebateEntity: DebateEntity){
            val side = args[0]
            val debateEntity = args[1]
            if ((side is Int) && (debateEntity is DebateEntity)){
                when(side){
                    1 -> {
                        var path_to_root_tp = thisDebateEntity.path_to_root
                        path_to_root_tp.plus(listOf(1, thisDebateEntity.side_1_entity.size))
                        debateEntity.path_to_root = path_to_root_tp
                        thisDebateEntity.side_1_entity?.add(debateEntity)
                    }
                    2 -> {
                        var path_to_root_tp = thisDebateEntity.path_to_root
                        path_to_root_tp.plus(listOf(2, thisDebateEntity.side_2_entity.size))
                        thisDebateEntity.side_2_entity?.add(debateEntity)
                    }
                    else -> {
                        //Log.i("temp_side", side.toString())
                        throw IllegalArgumentException("side should be either 1 or 2.")
                    }
                }
            }
        }
        fun removeArgument(args: List<Any>, thisDebateEntity: DebateEntity){
            val side = args[0]
            val position = args[1]
            if ((side is Int) && (position is Int)){
                when (side) {
                    1 -> {
                        thisDebateEntity.side_1_entity?.removeAt(position)
                    }
                    2 -> {
                        thisDebateEntity.side_2_entity?.removeAt(position)
                    }
                    else -> {
                        //Log.i("temp_side", side.toString())
                        throw IllegalArgumentException("side should be either 1 or 2.")
                    }
                }
            }
        }
        fun modify_title(args: List<Any>, thisDebateEntity: DebateEntity){
            val s = args[0]
            val start = args[1]
            val before = args[2]
            val count = args[3]
            if ((s is CharSequence) && (start is Int) && (before is Int) && (count is Int)){
                thisDebateEntity.title = thisDebateEntity.title.substring(0,start) + s.substring(start,start+count) +
                        thisDebateEntity.title.substring(start+before)
            }
        }
        fun modify_description(args: List<Any>, thisDebateEntity: DebateEntity){
            val s = args[0]
            val start = args[1]
            val before = args[2]
            val count = args[3]
            if ((s is CharSequence) && (start is Int) && (before is Int) && (count is Int)) {
                thisDebateEntity.description = thisDebateEntity.description.substring(0, start) + s.substring(
                    start,
                    start + count
                ) + thisDebateEntity.description.substring(start + before)
            }
        }

        fun translate_modify(args1: List<Any>, args2: List<Any>){
            if(args1[0] != args2[0]) {
                throw Exception("Strings should be identical")
            }
            val s = args1[0]
            val start_pos1 = args1[1]
            val start_pos2 = args2[1]
            val end_pos_bef1 = args1[2]
            val end_pos_bef2 = args2[2]
            val end_pos_new1 = args1[3]
            val end_pos_new2 = args2[3]
            if((s is CharSequence) && (start_pos1 is Int) && (start_pos2 is Int) && (end_pos_bef1 is Int)
                && (end_pos_bef2 is Int) && (end_pos_new1 is Int) && (end_pos_new2 is Int)){
                if (((start_pos1 >= start_pos2) && (start_pos1 <= end_pos_bef2)) ||
                    ((start_pos2 >= start_pos1) && (start_pos2 <= end_pos_bef1))){
                    // The string portion has been modified at the same time. Arbitrary choice should be made to decide
                    // which modification should be applied first. To that purpose, string are compared. The "smallest
                    // string modification is applied first. If strings are equal only one modification is applied.
                    val comp_str = s.subSequence(start_pos1, end_pos_new1).toString().compareTo(
                        s.subSequence(start_pos2, end_pos_new2).toString())
                    if (comp_str == 0){

                    }
                    // If comp_str == 0 the changes are identical. We cancel this change by doing nothing
                }
            }

            //TODO Si l'édition concurente porte sur des segement qui se chevauchent, il faut flaguer les changements dans l'historique
            // pour enuite revenir dessus plutard (quand?) et synchroniser le contenue avec les autres...
            // Pour savoir quel noeud impose sa version: pierre feuille ciseaux.
        }
    }
}