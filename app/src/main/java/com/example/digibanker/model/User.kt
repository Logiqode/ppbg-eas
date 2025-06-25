package com.example.digibanker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Long,
    val name: String,
    val email: String,
    val password: String,

    @SerialName("phone_number")
    val phoneNumber: String,

    @SerialName("birth_date")
    val birthDate: String
)