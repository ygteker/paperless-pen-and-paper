package lmu.msp.frontend.viewmodels

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.R
import java.util.*
import kotlin.collections.ArrayList

class HomeActivityViewModel(application: Application): AndroidViewModel(application) {

    fun getProfileListItems(): Array<String> {
        return getApplication<Application>().resources.getStringArray(R.array.profile_list_items)
    }
}