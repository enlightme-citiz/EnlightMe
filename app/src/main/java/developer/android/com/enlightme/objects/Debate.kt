package developer.android.com.enlightme.objects
import kotlinx.serialization.*

@Serializable
class Debate {
    var listAttendees = mutableListOf<Attendee>()
    var debateEntity = DebateEntity()
    var current_level = mutableListOf<MutableList<Int>>() // Used to track the current position in the debate. It consiste of a list of tuples(1or2 and Int). To find the current debate, go through the list. At each element the key give the side and the value, the position to go.

    fun get_debate_entity(): DebateEntity{
        // Return the debate entity that is currently on screen and edited.
        var curr_debate = debateEntity
        for(tup in current_level){
            if(tup[0] == 1){
                curr_debate = curr_debate.side_1_entity[tup[1]]
            }else{
                curr_debate = curr_debate.side_2_entity[tup[1]]
            }
        }
        return curr_debate
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
}