package developer.android.com.enlightme.objects

class DebateEntity {
    var idArg: Int = 0
    var title = ""
    var description = ""
    var side_1 = "Side 1"
    var side_2 = "Side 2"
    var side_1_entity = mutableListOf<DebateEntity>()
    var side_2_entity  = mutableListOf<DebateEntity>()
}