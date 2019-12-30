package developer.android.com.enlightme.Objects

import developer.android.com.enlightme.Utils.LiveMutableList
import kotlinx.serialization.Serializable

@Serializable
class UpdatePayload {
    constructor(hist_elt: HistElt, path_to_root: LiveMutableList<LiveMutableList<Int>>) {
        this.hist_elt = hist_elt
        this.path_to_root = path_to_root
    }

    var hist_elt: HistElt
    var path_to_root: LiveMutableList<LiveMutableList<Int>>
}
// Object that is send to the network and used to carry update informations (operation and state vector)