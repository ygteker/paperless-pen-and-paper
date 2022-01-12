package lmu.msp.frontend.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

class MessagesActivityViewModel(application: Application): AndroidViewModel(application) {

    fun getResources(): Resources {
        return getApplication<Application>().resources
    }

}