package developer.android.com.enlightme.objects
import kotlinx.serialization.*

@Serializable
class Debate {
    var listAttendees = mutableListOf<Attendee>()
    var debateEntity = DebateEntity()
}