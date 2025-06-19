package com.example.digibanker.data.repository

import android.util.Log
import com.example.digibanker.data.datasource.local.JsonDataSource
import com.example.digibanker.model.Account
import com.example.digibanker.model.Database
import com.example.digibanker.model.User

class BankRepository(private val dataSource: JsonDataSource) {

    suspend fun addAccountForUser(userId: Long) {
        val currentUsers = dataSource.getUsers()
        val currentAccounts = dataSource.getAccounts()

        val newAccount = Account(
            id = System.currentTimeMillis(),
            userId = userId,
            balance = 0.0
        )

        val updatedAccountList = currentAccounts + newAccount

        val newDatabaseState = Database(users = currentUsers, accounts = updatedAccountList)
        dataSource.saveDatabase(newDatabaseState)
    }

    suspend fun registerUser(newUser: User): Boolean {
        val currentUsers = dataSource.getUsers()
        if (currentUsers.any { it.email.equals(newUser.email, ignoreCase = true) }) {
            Log.w("BankRepository", "Registrasi gagal: Email ${newUser.email} sudah ada.")
            return false
        }

        val updatedUserList = currentUsers + newUser
        val currentAccounts = dataSource.getAccounts()

        val newAccount = Account(
            id = System.currentTimeMillis() + 1,
            userId = newUser.id,
            balance = 0.0
        )
        val updatedAccountList = currentAccounts + newAccount

        val newDatabaseState = Database(users = updatedUserList, accounts = updatedAccountList)
        dataSource.saveDatabase(newDatabaseState)
        return true
    }

    suspend fun getUsers(): List<User> {
        return dataSource.getUsers()
    }

    suspend fun getUser(userId: Long): User? {
        return dataSource.getUsers().find { it.id == userId }
    }

    suspend fun getAccountsForUser(userId: Long): List<Account> {
        val userExists = dataSource.getUsers().any { it.id == userId }
        if (!userExists) return emptyList()
        return dataSource.getAccounts().filter { it.userId == userId }
    }

    suspend fun getAccount(accountId: Long): Account? {
        return dataSource.getAccounts().find { it.id == accountId }
    }

    suspend fun login(email: String, password: String): User? {
        val user = dataSource.getUsers().find { it.email.equals(email, ignoreCase = true) }
        return if (user != null && user.password == password) user else null
    }

    suspend fun performTransfer(fromAccountId: Long, toAccountId: Long, amount: Double): Boolean {
        val allAccounts = dataSource.getAccounts().toMutableList()
        val allUsers = dataSource.getUsers()
        val fromIndex = allAccounts.indexOfFirst { it.id == fromAccountId }
        val toIndex = allAccounts.indexOfFirst { it.id == toAccountId }

        if (fromIndex == -1 || toIndex == -1 || allAccounts[fromIndex].balance < amount) {
            return false
        }

        val fromAccount = allAccounts[fromIndex]
        val toAccount = allAccounts[toIndex]
        allAccounts[fromIndex] = fromAccount.copy(balance = fromAccount.balance - amount)
        allAccounts[toIndex] = toAccount.copy(balance = toAccount.balance + amount)

        val newDatabaseState = Database(users = allUsers, accounts = allAccounts)
        dataSource.saveDatabase(newDatabaseState)
        return true
    }
}