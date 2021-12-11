package com.example.mipokechido

import android.app.Application
import com.example.mipokechido.database.DatabaseManager

open class MyApplication: Application() {
    override fun onCreate() {
        DatabaseManager.instance.initializeDb(applicationContext)
        super.onCreate()
    }
}