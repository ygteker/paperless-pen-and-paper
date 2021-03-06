package lmu.msp.frontend.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lmu.msp.frontend.api.model.User
/**
 * This defines a viewModel that contains a user object that can be used as a SharedViewModel
 * @author Valentin Scheibe
 */
class UserViewModel : ViewModel() {
    private var _userData = MutableLiveData<User>()
    val userData: MutableLiveData<User> = _userData

    fun setUser(user: User) {
        _userData.value = user
    }

}