package developer.android.com.enlightme.objects

class DebatEntity {
    lateinit var title: String
    lateinit var description: String
    lateinit var side_1: String
    lateinit var side_2: String
    lateinit var side_1_entity: MutableList<DebatEntity>
    lateinit var side_2_entity: MutableList<DebatEntity>
}