package lmu.msp.frontend.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.R
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider

class HomeFragment : Fragment() {

    private lateinit var userApi: PenAndPaperApiInterface.UserApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        userApi = RetrofitProvider().getUserApi(view.context)

        userApi.getAuthenticatedHelloWorld()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Log.i("abc", "errorororor ${it.message}")

            }
            .doOnSuccess { Log.i("abc", it) }
            .subscribe { t1, t2 -> Log.i("abc", "subscr: $t1") }

        return view
    }
}