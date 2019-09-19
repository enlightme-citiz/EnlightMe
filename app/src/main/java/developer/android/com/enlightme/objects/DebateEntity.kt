package developer.android.com.enlightme.objects

import kotlinx.serialization.Serializable

@Serializable
class DebateEntity {
    var idArg: Int = 0
    var title = ""
    var description = ""
    var side_1 = "Side 1"
    var side_2 = "Side 2"
    var side_1_entity = mutableListOf<DebateEntity>()
    var side_2_entity  = mutableListOf<DebateEntity>()

    // Every necerassary function to change the debate are stored here. These function does not change the appearance
    // on UI but just the data. It enables dealing with the history and concurent editing
    fun addArgument(side: Int, debateEntity: DebateEntity){
        when(side){
            1 -> {
                //val place = viewModel.debate.value?.debateEntity?.side_1_entity?.size ?: -1
                this.side_1_entity?.add(debateEntity)
            }
            2 -> {
                //val place = viewModel.debate.value?.debateEntity?.side_2_entity?.size ?: -1
                this.side_2_entity?.add(debateEntity)
            }
            else -> {
                //Log.i("temp_side", side.toString())
                throw IllegalArgumentException("side should be either 1 or 2.")
            }
        }
    }
    //TODO add other functions like:
    // delete argument in side entities
    // del in title
    // add in title
    // del in description
    // add in description

}