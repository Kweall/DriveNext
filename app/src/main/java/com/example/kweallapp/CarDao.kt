package com.example.kweallapp

import androidx.room.*

@Dao
interface CarDao {
    @Query("SELECT * FROM cars WHERE id = :carId LIMIT 1")
    fun getCarById(carId: Int): Car?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car): Long

    @Update
    suspend fun update(car: Car): Int

    @Delete
    suspend fun delete(car: Car): Int
}
