package com.example.kweallapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val model: String,
    val brand: String,
    val pricePerDay: Int,
    val transmission: String,
    val fuelType: String,
    @ColumnInfo(name = "image")  // Явное указание имени колонки
    val image: Int
)