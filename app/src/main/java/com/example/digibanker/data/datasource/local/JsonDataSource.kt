package com.example.digibanker.data.datasource.local

import android.content.Context
import android.util.Log
import com.example.digibanker.model.Database
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * A data source that reads and parses bank data from a local JSON file.
 *
 * This class requires the application context to access the 'assets' folder.
 *
 * @param context The application context, needed to open the assets.
 */
class JsonDataSource(private val context: Context) {

    // A tag for our logs, making them easy to find.
    private val logTag = "DigiBankerLog"

    // A lazy-loaded property to hold the parsed database data.
    // This means the JSON is only read and parsed once, the first time it's accessed.
    private val database: Database? by lazy {
        parseJsonFromAssets("FakeDatabase.json")
    }

    /**
     * Reads a JSON file from the app's assets folder and parses it into a Database object.
     *
     * @param fileName The name of the JSON file in the 'assets' folder.
     * @return A parsed [Database] object, or null if an error occurs.
     */
    private fun parseJsonFromAssets(fileName: String): Database? {
        return try {
            Log.d(logTag, "Attempting to read and parse $fileName...")
            // 1. Open an InputStream to the file in the assets folder.
            val inputStream = context.assets.open(fileName)

            // 2. Read the entire content of the file.
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // 3. Configure the Json parser to be lenient.
            val json = Json { ignoreUnknownKeys = true }

            // 4. Decode the JSON string into our Database data class.
            val parsedData = json.decodeFromString<Database>(jsonString)
            Log.d(logTag, "Successfully parsed $fileName. Found ${parsedData.users.size} users and ${parsedData.accounts.size} accounts.")
            parsedData

        } catch (e: IOException) {
            // Handle cases where the file cannot be read (e.g., file not found).
            Log.e(logTag, "Error reading asset file: $fileName. Check if the file exists in the correct 'assets' folder.", e)
            null
        } catch (e: kotlinx.serialization.SerializationException) {
            // Handle cases where the JSON format is invalid.
            Log.e(logTag, "Error parsing JSON from file: $fileName. Check if the JSON format is valid.", e)
            null
        }
    }

    /**
     * Public function to get all users from the parsed database.
     * Returns an empty list if the database could not be loaded.
     */
    fun getUsers() = database?.users ?: emptyList()

    /**
     * Public function to get all accounts from the parsed database.
     * Returns an empty list if the database could not be loaded.
     */
    fun getAccounts() = database?.accounts ?: emptyList()
}