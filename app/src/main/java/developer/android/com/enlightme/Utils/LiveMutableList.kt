package developer.android.com.enlightme.Utils

import android.util.Log
import androidx.databinding.BaseObservable
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
class LiveMutableList<T>: MutableList<T> by mutableListOf() {
    //var iteration = 0
    fun add(element: T, baseObservable: @Polymorphic BaseObservable): Boolean {
        baseObservable.notifyChange()
        Log.i("LiveMutableList", "LiveMutableList add function")
        return this.add(element)
    }

    fun add(index: Int, element: T, baseObservable: @Polymorphic BaseObservable) {
        baseObservable.notifyChange()
        Log.i("LiveMutableList", "LiveMutableList add function with index")
        //if(iteration>0){
        //    throw Exception("Not good in here")
        //}
        //iteration += 1
        this.add(index, element)
    }

    fun removeAt(index: Int, baseObservable: @Polymorphic BaseObservable): T {
        baseObservable.notifyChange()
        return this.removeAt(index)
    }

    fun remove(element: T, baseObservable: @Polymorphic BaseObservable): Boolean {
        baseObservable.notifyChange()
        return this.remove(element)
    }
}