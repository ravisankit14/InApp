package com.retailio.retailioinapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.retailio.retailioinapp.InAppApplication
import com.retailio.retailioinapp.R
import com.retailio.retailioinapp.roomdb.InAppRoomDatabase

class InAppCentreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_centre)

        //(applicationContext as InAppApplication).dbInstance()
    }
}