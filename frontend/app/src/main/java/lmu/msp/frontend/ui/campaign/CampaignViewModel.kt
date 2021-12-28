package lmu.msp.frontend.ui.campaign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CampaignViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the campaign Fragment"
    }
    val text: LiveData<String> = _text
}