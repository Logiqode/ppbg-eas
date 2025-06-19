package com.example.digibanker

import android.app.Application
import com.example.digibanker.data.datasource.local.JsonDataSource
import com.example.digibanker.data.repository.BankRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApplication : Application() {

    companion object {
        lateinit var repository: BankRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()

        val dataSource = JsonDataSource(applicationContext)
        repository = BankRepository(dataSource)

        GlobalScope.launch {
            repository.getUsers()
        }
    }
}