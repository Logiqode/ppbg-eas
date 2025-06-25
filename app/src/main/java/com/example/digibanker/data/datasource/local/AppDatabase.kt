package com.example.digibanker.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.digibanker.model.Account
import com.example.digibanker.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File


@Database(entities = [User::class, Account::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bankDao(): BankDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "digibanker_database"
                )
                    .addCallback(object: Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            GlobalScope.launch(Dispatchers.IO) {
                                val jsonString = context.assets.open("FakeDatabase.json").bufferedReader().use { it.readText() }
                                val databaseData = Json.decodeFromString<com.example.digibanker.model.Database>(jsonString)
                                getDatabase(context).bankDao().getUsers().let {
                                    if(it.isEmpty()) {
                                        databaseData.users.forEach { user ->
                                            getDatabase(context).bankDao().insertUser(user)
                                        }
                                        databaseData.accounts.forEach { account ->
                                            getDatabase(context).bankDao().insertAccount(account)
                                        }
                                    }
                                }

                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}