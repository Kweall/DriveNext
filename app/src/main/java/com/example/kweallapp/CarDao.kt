package com.example.kweallapp

import androidx.room.*

@Dao
interface CarDao {
    @Query("SELECT * FROM cars WHERE brand LIKE :query OR model LIKE :query")
    suspend fun searchCars(query: String): List<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg cars: Car)

    @Update
    suspend fun update(car: Car): Int

    @Delete
    suspend fun delete(car: Car): Int

    @Query("SELECT * FROM cars")
    suspend fun getAllCars(): List<Car>
}
