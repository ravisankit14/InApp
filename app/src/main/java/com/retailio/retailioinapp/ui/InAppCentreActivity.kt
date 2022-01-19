package com.retailio.retailioinapp.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.retailio.retailioinapp.R
import com.retailio.retailioinapp.databinding.ActivityInAppCentreBinding
import com.retailio.retailioinapp.enum.InAppLocationType
import com.retailio.retailioinapp.retrofit.WEBVIEW_APP
import com.retailio.retailioinapp.retrofit.addFragment
import com.retailio.retailioinapp.roomdb.InAppRoomDatabase
import com.retailio.retailioinapp.roomdb.entity.NotificationInAppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InAppCentreActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityInAppCentreBinding
    private var mData: NotificationInAppEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInAppCentreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.echoCenter.ivCross.setOnClickListener(this)
        binding.echoCenter.tvDeeplink.setOnClickListener(this)

        fetchInAppData()
        setData()
    }

    private fun setData(){

        _inAppData.observe(this, {
            it?.let {
                mData = it
                if(mData?.buttonLabel.isNullOrEmpty())
                    binding.echoCenter.tvDeeplink.visibility = View.GONE

                if(mData?.deeplink.isNullOrEmpty())
                    mData?.buttonLabel = getString(R.string.okay_got_it)

                if(mData?.header.isNullOrEmpty())
                    binding.echoCenter.tvTitle.visibility = View.GONE

                if(mData?.body.isNullOrEmpty())
                    binding.echoCenter.tvDescription.visibility = View.GONE

                binding.echoCenter.tvDescription.text = mData?.body
                binding.echoCenter.tvTitle.text = mData?.header
                binding.echoCenter.tvDeeplink.text = mData?.buttonLabel
                Glide.with(this)
                    .applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_launcher))
                    .load(mData?.imageUrl)
                    .into(binding.echoCenter.ivThumbnail)

                //InAppSessionService.IN_APP_NOTIF_VIEW_COUNT_SESSION += 1
                updateData(mData!!._id)
            }
        })
    }

    private fun updateData(id: Long) = lifecycleScope.launch(Dispatchers.IO) {
        InAppRoomDatabase.getInstance(applicationContext)?.notificationInAppDao()?.updateNotification(id)
    }

    private fun fetchInAppData(){
        lifecycleScope.launch(Dispatchers.IO) {
            val data = InAppRoomDatabase.getInstance(applicationContext)?.notificationInAppDao()?.getNotification(0,
                InAppLocationType.HOMEPAGE.name,  System.currentTimeMillis())

            data.let { inAppData.postValue(it) }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.echoCenter.ivCross.id -> {
                finish()
            }
            binding.echoCenter.tvDeeplink.id -> {
                if (mData?.deeplink!!.isNotBlank()) {
                    val deepLinkUrl = mData?.deeplink!!.trim { it <= ' ' }
                    val externalLink = Uri.parse(deepLinkUrl)
                    if (externalLink.queryParameterNames.contains(WEBVIEW_APP)) {
                        //addFragment(DynamicWebViewFragment.getInstance("Deeplink",deepLinkUrl))
                    }else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mData?.deeplink))
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        if (intent.resolveActivity(this.packageManager) != null)
                            startActivity(intent)
                        finish()
                    }
                }else{
                    finish()
                }
            }
        }
    }

    private var inAppData = MutableLiveData<NotificationInAppEntity>()
    private val _inAppData: LiveData<NotificationInAppEntity> = inAppData
}