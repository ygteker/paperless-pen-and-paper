package lmu.msp.frontend.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.helpers.auth0.ApiInterface
import lmu.msp.frontend.models.Message
import lmu.msp.frontend.models.MessageModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessagesViewModel: ViewModel() {

    var lst = MutableLiveData<ArrayList<MessageModel>>()
    var newList = arrayListOf<MessageModel>()

    fun add(message: MessageModel){
        newList.add(message)
        lst.value = newList
    }

    fun remove(message: MessageModel) {
        newList.remove(message)
        lst.value = newList
    }

    fun getMessages(accessToken: String) {
        var apiInterface: Call<List<Message>> = ApiInterface.create().getMessages(accessToken)
        apiInterface.enqueue(object: Callback<List<Message>>{
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.body() != null) {
                    val arr = response.body()!!
                    for (item in arr) {
                        add(MessageModel(item.id, item.receiver.toString(), "Title", item.message, false))
                    }
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                print(t.message)
            }
        })
    }

    fun deleteMessage(accessToken: String, messageModel: MessageModel) {
        var apiInterface: Call<String> = ApiInterface.create().deleteMessage(accessToken, messageModel.id)
        apiInterface.enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    remove(messageModel)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                print(t.message)
            }
        })
    }
}