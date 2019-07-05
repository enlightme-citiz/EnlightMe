package developer.android.com.enlightme.objects

class Debate {
    var listAttendees : MutableList<Attendee>
    lateinit var debatEntity: DebatEntity
    constructor(){
        this.listAttendees = mutableListOf<Attendee>()
    }
}