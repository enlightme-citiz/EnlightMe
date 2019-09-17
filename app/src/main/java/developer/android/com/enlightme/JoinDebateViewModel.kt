package developer.android.com.enlightme

import androidx.lifecycle.ViewModel

class JoinDebateViewModel : ViewModel() {
    // The username
    var userName : String = ""
    // Dict of name network => list of endPointId
    var listNet = mutableMapOf<String, MutableList<String>>()
}