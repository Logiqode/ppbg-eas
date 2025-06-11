package com.example.digibanker.data.repository

import com.example.digibanker.data.datasource.local.JsonDataSource
import com.example.digibanker.model.Account
import com.example.digibanker.model.User

/**
 * Repository that acts as a single source of truth for bank-related data.
 *
 * It abstracts the data source from the rest of the app. ViewModels will interact
 * with this repository to get data, without needing to know where the data comes from
 * (e.g., a local JSON file, a remote API, or a database).
 *
 * @param dataSource The local data source for fetching raw user and account data.
 */
class BankRepository(private val dataSource: JsonDataSource) {

    /**
     * Finds a specific user by their unique ID.
     *
     * @param userId The ID of the user to find.
     * @return The [User] object if found, otherwise null.
     */
    fun getUser(userId: Long): User? {
        return dataSource.getUsers().find { it.id == userId }
    }

    /**
     * Retrieves all accounts associated with a specific user ID.
     *
     * @param userId The ID of the user whose accounts are to be retrieved.
     * @return A list of [Account] objects belonging to the user. Returns an empty list
     * if the user has no accounts or the user ID is not found.
     */
    fun getAccountsForUser(userId: Long): List<Account> {
        // First, ensure the user exists. While not strictly necessary for this logic,
        // it's a good practice for potential future validation.
        val userExists = dataSource.getUsers().any { it.id == userId }
        if (!userExists) {
            return emptyList()
        }

        // Filter the full list of accounts to find the ones matching the user's ID.
        return dataSource.getAccounts().filter { it.userId == userId }
    }

    /**
     * Retrieves a specific account by its unique ID.
     *
     * @param accountId The ID of the account to find.
     * @return The [Account] object if found, otherwise null.
     */
    fun getAccount(accountId: Long): Account? {
        return dataSource.getAccounts().find { it.id == accountId }
    }
}
