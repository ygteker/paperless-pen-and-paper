package lmu.msp.frontend.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.api.model.User

class UserViewModel : ViewModel() {
    private var _userData = MutableLiveData<User>()
    val userData: MutableLiveData<User> = _userData

    val testString = "LoremIpsum"

    fun setUser(user: User) {
        _userData.value = user
    }

  //  fun getUser(): LiveData<User> = userData

}