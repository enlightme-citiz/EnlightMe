package developer.android.com.enlightme.objects
import kotlinx.serialization.*

@Serializable
class Debate {
    var listAttendees = mutableListOf<Attendee>()
    var debateEntity = DebateEntity()
    var current_level = mutableListOf<MutableList<Int>>() // Used to track the current position in the debate. It consiste of a list of tuples(1or2 and Int). To find the current debate, go through the list. At each element the key give the side and the value, the position to go.
}