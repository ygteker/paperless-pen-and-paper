package lmu.msp.frontend.helpers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import lmu.msp.frontend.Constants.Companion.SHARED_PATH_USER_TOKEN
import lmu.msp.frontend.R


class TokenManager(context: Context) {

    companion object{
        private const val TAG = "TokenManager"
    }


    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name), Context.MODE_PRIVATE
    )

    fun save(token: String) {
        Log.i(TAG,"save")
        //write token to shared preferences (they are in private mode)
        sharedPreferences.edit().putString(SHARED_PATH_USER_TOKEN, token).apply()
    }

    fun load(): String? {
        Log.i(TAG,"load")
        return sharedPreferences.getString(SHARED_PATH_USER_TOKEN, null)
    }

    fun deleteToken() {
        Log.i(TAG,"delete")
        sharedPreferences.edit().remove(SHARED_PATH_USER_TOKEN).apply()
    }
}