package com.example.digibanker

import android.app.Application
import com.example.digibanker.data.datasource.local.AppDatabase
import com.example.digibanker.data.repository.BankRepository

class MyApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { BankRepository(database.bankDao()) }
}