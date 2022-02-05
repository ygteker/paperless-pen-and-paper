package lmu.msp.frontend.ui.profile

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import lmu.msp.frontend.BuildConfig
import lmu.msp.frontend.api.PenAndPaperApiInterface
import lmu.msp.frontend.databinding.ActivityInfoBinding
import lmu.msp.frontend.helpers.InfoViewAdapter
import lmu.msp.frontend.helpers.retrofit.RetrofitProvider

class InfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var infoViewAdapter: InfoViewAdapter
    private lateinit var userApi: PenAndPaperApiInterface.UserApi
    private var infos = mutableListOf<InfoElement>()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userApi = RetrofitProvider(this).getUserApi()
        userApi.getUser()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                infos.add(InfoElement("Version", BuildConfig.VERSION_NAME))
                infos.add(InfoElement("Campaigns participated", it.campaignMember.size.toString()))
                infos.add(InfoElement("Campaigns owned", it.campaignOwner.size.toString()))
                infos.add(InfoElement("Mails sent", it.sendMails.size.toString()))
                infos.add(InfoElement("Mails received", it.receivedMails.size.toString()))
                initInfoView(infos.toList())
            }
            .subscribe()
    }

    private fun initInfoView(infos: List<InfoElement>) {
        infoViewAdapter = InfoViewAdapter(infos)
        binding.infoView.adapter = infoViewAdapter
    }
}