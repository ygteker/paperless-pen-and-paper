package lmu.msp.frontend.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.api.model.User

class CampaignActivityViewModel : ViewModel() {
    private var _campaignId = MutableLiveData(1L)
    val campaignId: LiveData<Long> = _campaignId

    fun setCampaignId(campaignIdSet: Long) {
        _campaignId.value = campaignIdSet
    }
}