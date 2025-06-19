package com.example.digibanker.model

import kotlinx.serialization.Serializable

@Serializable
data class  Database(
    val users: List<User>,
    val accounts: List<Account>
)
