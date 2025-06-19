package com.example.digibanker.data.datasource.local

import android.content.Context
import android.util.Log
import com.example.digibanker.model.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class JsonDataSource(private val context: Context) {

    private val logTag = "DigiBankerLog"
    private val internalFileName = "DigiBankerDatabase.json"
    private val internalFile = File(context.filesDir, internalFileName)

    private var database: Database? = null

    private suspend fun copyDatabaseFromAssetsIfNeeded() {
        if (internalFile.exists()) return

        Log.d(logTag, "File di internal storage tidak ditemukan. Menyalin dari assets...")
        withContext(Dispatchers.IO) {
            try {
                context.assets.open("FakeDatabase.json").use { inputStream ->
                    internalFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Log.d(logTag, "Berhasil menyalin database ke penyimpanan internal.")
            } catch (e: IOException) {
                Log.e(logTag, "Gagal menyalin database dari assets.", e)
            }
        }
    }

    private suspend fun loadAndParseJsonIfNeeded() {
        copyDatabaseFromAssetsIfNeeded()

        if (database != null) return

        database = withContext(Dispatchers.IO) {
            try {
                Log.d(logTag, "Membaca dan parsing JSON dari penyimpanan internal...")
                val jsonString = internalFile.readText()
                Json { ignoreUnknownKeys = true }.decodeFromString<Database>(jsonString)
            } catch (e: Exception) {
                Log.e(logTag, "Gagal memuat database dari penyimpanan internal.", e)
                null
            }
        }
    }

    suspend fun saveDatabase(newDatabaseState: Database) {
        withContext(Dispatchers.IO) {
            try {
                Log.d(logTag, "Menyimpan database ke penyimpanan internal...")
                val jsonString = Json.encodeToString(newDatabaseState)
                internalFile.writeText(jsonString)
                database = newDatabaseState
                Log.d(logTag, "Berhasil menyimpan database.")
            } catch (e: Exception) {
                Log.e(logTag, "Gagal menyimpan database.", e)
            }
        }
    }

    suspend fun getUsers(): List<com.example.digibanker.model.User> {
        loadAndParseJsonIfNeeded()
        return database?.users ?: emptyList()
    }

    suspend fun getAccounts(): List<com.example.digibanker.model.Account> {
        loadAndParseJsonIfNeeded()
        return database?.accounts ?: emptyList()
    }
}