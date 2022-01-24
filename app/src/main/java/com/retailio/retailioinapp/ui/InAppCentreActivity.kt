package com.retailio.retailioinapp.ui

import android.content.Context
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
import com.retailio.retailioinapp.enum.InAppSessionService
import com.retailio.retailioinapp.retrofit.WEBVIEW_APP
import com.retailio.retailioinapp.retrofit.addFragment
import com.retailio.retailioinapp.roomdb.InAppRoomDatabase
import com.retailio.retailioinapp.roomdb.entity.NotificationInAppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InAppCentreActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityInAppCentreBinding
    private var mData: NotificationInAppEntity? = null

    companion object {
        private const val IN_APP_CENTER = "in_app_center"
        @JvmStatic
        fun getLaunchIntent(context: Context, it: NotificationInAppEntity): Intent {
            val intent = Intent(context, InAppCentreActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra(IN_APP_CENTER, it)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInAppCentreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.echoCenter.ivCross.setOnClickListener(this)
        binding.echoCenter.tvDeeplink.setOnClickListener(this)

        mData = intent?.getParcelableExtra(IN_APP_CENTER)
        setData()
    }

    private fun setData(){

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

        InAppSessionService.IN_APP_NOTIF_VIEW_COUNT_SESSION += 1
        updateData(mData!!._id)
    }

    private fun updateData(id: Long) = lifecycleScope.launch(Dispatchers.IO) {
        InAppRoomDatabase.getInstance(applicationContext).notificationInAppDao().updateNotification(id)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.echoCenter.ivCross.id -> {
                finish()
            }
            binding.echoCenter.tvDeeplink.id -> {
                if (mData?.deeplink!!.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mData?.deeplink))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    if (intent.resolveActivity(this.packageManager) != null)
                        startActivity(intent)
                    finish()
                }else{
                    finish()
                }
            }
        }
    }
}