package com.example.digibanker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("birth_date")
    val birthDate: String
)
