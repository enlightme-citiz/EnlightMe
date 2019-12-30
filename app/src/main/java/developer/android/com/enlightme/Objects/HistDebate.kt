package developer.android.com.enlightme.Objects

import kotlinx.serialization.Serializable

@Serializable
class HistDebate {
    var histEltList = mutableListOf<HistElt>()
    fun getNbChange(id_author: String): Int{
        var count = 0
        for (e in histEltList){
           if (e.id_author == id_author){
               count += 1
           }
        }
        return count
    }
}