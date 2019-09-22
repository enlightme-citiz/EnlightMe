package developer.android.com.enlightme.objects

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

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
    var state_vector = mutableMapOf<String, Int>()
    //TODO initialise state vector when the debate is first created and when joining the debate or at least update it.
    fun send_update(context: Context, list_endpointId:List<String>){
        val json = Json(JsonConfiguration.Stable)
        // TODO Intitialize val state_vector
        val update_payload = UpdatePayload(hist_debate.histEltList[hist_debate.histEltList.size-1], state_vector, path_to_root)
        val pld = Payload.fromBytes(json.stringify(UpdatePayload.serializer(),
            update_payload).toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(list_endpointId, pld)
    }
    fun applyUpdateItem(updatePayload: UpdatePayload){
        val hist_elt = updatePayload.hist_elt
        val state_vector = updatePayload.state_vector
        //TODO apply update
        // Check that the number of change made by the author of hist_elt is >
    }
    fun send_whole_debate(context: Context, endpointId: String){
        val json = Json(JsonConfiguration.Stable)
        var pld = Payload.fromBytes(json.stringify(serializer(),
            this).toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(endpointId, pld)
        pld = Payload.fromBytes(endpointId.toByteArray(Charsets.UTF_8))
        Nearby.getConnectionsClient(context).sendPayload(endpointId, pld)
    }
    // Every necerassary function to change the debate are stored here. These function does not change the appearance
    // on UI but just the data. It enables dealing with the history and concurent editing
    fun addArgument(side: Int, debateEntity: DebateEntity){
        when(side){
            1 -> {
                var path_to_root_tp = path_to_root
                path_to_root_tp.plus(listOf(1, side_1_entity.size))
                debateEntity.path_to_root = path_to_root_tp
                side_1_entity?.add(debateEntity)
            }
            2 -> {
                var path_to_root_tp = path_to_root
                path_to_root_tp.plus(listOf(2, side_2_entity.size))
                side_2_entity?.add(debateEntity)
            }
            else -> {
                //Log.i("temp_side", side.toString())
                throw IllegalArgumentException("side should be either 1 or 2.")
            }
        }
    }
    fun removeArgument(side:Int, position:Int){
        when(side){
            1 -> {
                side_1_entity?.removeAt(position)
            }
            2 -> {
                side_2_entity?.removeAt(position)
            }
            else -> {
                //Log.i("temp_side", side.toString())
                throw IllegalArgumentException("side should be either 1 or 2.")
            }
        }
    }
    fun modify_title(s: CharSequence,
                     start: Int,
                     before: Int,
                     count: Int){
        title = title.substring(0,start) + s.substring(start,start+count) + title.substring(start+before)
    }
    fun modify_description(s: CharSequence,
                     start: Int,
                     before: Int,
                     count: Int){
        description = description.substring(0,start) + s.substring(start,start+count) + description.substring(start+before)
    }
}