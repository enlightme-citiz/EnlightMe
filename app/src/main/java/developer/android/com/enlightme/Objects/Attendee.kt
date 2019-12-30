package developer.android.com.enlightme.Objects

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.serialization.Serializable

@Serializable
class Attendee: BaseObservable() {
    // Class modeling people connected to the debate
    @Bindable
    var name = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }
}