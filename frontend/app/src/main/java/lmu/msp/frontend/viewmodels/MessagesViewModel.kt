package lmu.msp.frontend.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.models.MessageModel

class MessagesViewModel: ViewModel() {

    var lst = MutableLiveData<ArrayList<MessageModel>>()
    var newlist = arrayListOf<MessageModel>()

    fun add(message: MessageModel){
        newlist.add(message)
        lst.value=newlist
    }
}