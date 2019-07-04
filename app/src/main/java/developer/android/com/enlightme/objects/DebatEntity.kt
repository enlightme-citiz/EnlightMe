package developer.android.com.enlightme.objects

class DebatEntity {
    lateinit var title: String
    lateinit var description: String
    lateinit var prosEntity: MutableList<DebatEntity>
    lateinit var consEntity: MutableList<DebatEntity>
}