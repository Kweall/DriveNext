package com.example.kweallapp

import androidx.room.TypeConverter
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val patronymicName: String,
    val birthDate: String,
    val gender: String,
    val driverLicenseNumber: String,
    val driverLicenseIssueDate: String,
    val registrationDate: String,
    val avatar: String, // Путь к аватарке
    val middleName: String = "", // Используйте пустую строку
    val driverLicensePhoto: String = "", // Используйте пустую строку
    val passportPhoto: String = "", // Используйте пустую строку
    val gmail: String = ""
)