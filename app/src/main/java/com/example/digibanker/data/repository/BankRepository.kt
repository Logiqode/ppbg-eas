package com.example.digibanker.data.repository

import com.example.digibanker.data.datasource.local.BankDao
import com.example.digibanker.model.Account
import com.example.digibanker.model.User

class BankRepository(private val bankDao: BankDao) {

    suspend fun addAccountForUser(userId: Long) {
        val newAccount = Account(
            id = System.currentTimeMillis(),
            userId = userId,
            balance = 0.0
        )
        bankDao.insertAccount(newAccount)
    }

    suspend fun registerUser(newUser: User): Boolean {
        if (bankDao.getUserByEmail(newUser.email) != null) {
            return false // Email sudah ada
        }
        bankDao.insertUser(newUser)
        // Buat akun pertama untuk user baru
        addAccountForUser(newUser.id)
        return true
    }

    suspend fun getUsers(): List<User> {
        return bankDao.getUsers()
    }

    suspend fun getUser(userId: Long): User? {
        return bankDao.getUser(userId)
    }

    suspend fun getAccountsForUser(userId: Long): List<Account> {
        return bankDao.getAccountsForUser(userId)
    }

    suspend fun getAccount(accountId: Long): Account? {
        return bankDao.getAccount(accountId)
    }

    suspend fun login(email: String, password: String): User? {
        val user = bankDao.getUserByEmail(email)
        return if (user != null && user.password == password) user else null
    }

    suspend fun performTransfer(fromAccountId: Long, toAccountId: Long, amount: Double): Boolean {
        val fromAccount = bankDao.getAccount(fromAccountId)
        val toAccount = bankDao.getAccount(toAccountId)

        if (fromAccount == null || toAccount == null || fromAccount.balance < amount) {
            return false
        }

        val updatedFromAccount = fromAccount.copy(balance = fromAccount.balance - amount)
        val updatedToAccount = toAccount.copy(balance = toAccount.balance + amount)

        bankDao.updateAccount(updatedFromAccount)
        bankDao.updateAccount(updatedToAccount)
        return true
    }
}