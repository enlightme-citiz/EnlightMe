package developer.android.com.enlightme.objects

import kotlinx.serialization.Serializable

@Serializable
class HistDebate {
    var hist_debate = mutableListOf<HistElt>()

    fun applyUpdate(new_hist: MutableList<HistElt>){
        //TODO apply update
        //Find every novelties
        // For each author of this list, check we do not miss anything with the number of changes stored in HistElt.
        // Otherwise, wait for a new HistDebate with missing updates. Mettre dans une pile d'attente
        // If we have changes in our list that are present but not in the new_hist, apply changes with transform function
        // Oterwise juste apply the change
    }
}