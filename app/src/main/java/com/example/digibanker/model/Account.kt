package com.example.digibanker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Long,

    @SerialName("user_id")
    val userId: Long,

    val balance: Double
)
