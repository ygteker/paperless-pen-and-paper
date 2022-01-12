package lmu.msp.frontend.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.api.model.User

class UserViewModel : ViewModel() {
    private var userData = MutableLiveData<User>()

    fun setUser(user: User) {
        userData.value = user
    }

    fun getUser(): LiveData<User> = userData

}