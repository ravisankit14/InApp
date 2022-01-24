package com.retailio.retailioinapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.retailio.retailioinapp.R
import com.retailio.retailioinapp.databinding.ActivityInAppMiniBinding
import com.retailio.retailioinapp.enum.InAppSessionService
import com.retailio.retailioinapp.roomdb.InAppRoomDatabase
import com.retailio.retailioinapp.roomdb.entity.NotificationInAppEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InAppMiniFragment : Fragment(), View.OnClickListener {

    private var mData: NotificationInAppEntity? = null
    private lateinit var binding: ActivityInAppMiniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mData = it.getParcelable(IN_APP_MINI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityInAppMiniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.echoMini.ivCross.setOnClickListener(this)
        binding.echoMini.tvDeeplink.setOnClickListener(this)

        if(mData?.buttonLabel.isNullOrEmpty())
            binding.echoMini.tvDeeplink.visibility = View.GONE

        if(mData?.deeplink.isNullOrEmpty())
            mData?.buttonLabel = getString(R.string.okay_got_it)

        if(mData?.header.isNullOrEmpty())
            binding.echoMini.tvTitle.visibility = View.GONE

        if(mData?.body.isNullOrEmpty())
            binding.echoMini.tvDescription.visibility = View.GONE

        binding.echoMini.tvDescription.text = mData?.body
        binding.echoMini.tvTitle.text = mData?.header
        binding.echoMini.tvDeeplink.text = mData?.buttonLabel
        Glide.with(this)
            .applyDefaultRequestOptions(RequestOptions().placeholder(R.mipmap.ic_launcher))
            .load(mData?.imageUrl)
            .into(binding.echoMini.ivThumbnail)

        InAppSessionService.IN_APP_NOTIF_VIEW_COUNT_SESSION += 1
        updateData(mData!!._id)

    }

    private fun updateData(id: Long) = lifecycleScope.launch(Dispatchers.IO) {
        InAppRoomDatabase.getInstance(requireActivity()).notificationInAppDao().updateNotification(id)
    }

    companion object {
        private const val IN_APP_MINI = "in_app_mini"
        @JvmStatic
        fun getInstance(it: NotificationInAppEntity): InAppMiniFragment {
            return InAppMiniFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IN_APP_MINI, it)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.echoMini.ivCross.id -> {
                closeFragment()
            }
            binding.echoMini.tvDeeplink.id -> {
                if (mData?.deeplink!!.isNotBlank()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mData?.deeplink))
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    if (intent.resolveActivity(requireActivity().packageManager) != null)
                        startActivity(intent)
                    closeFragment()
                }else{
                    closeFragment()
                }
            }
        }
    }

    private fun closeFragment(){
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }
}