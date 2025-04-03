package com.example.kweallapp

import android.content.Context
import kotlinx.coroutines.runBlocking

fun initializeCars(database: AppDatabase) {
        val carDao = database.carDao()
        val initialCars = listOf(
            Car(
                0,
                "S 500 Sedan",
                "Mercedes-Benz",
                2500,
                "А/Т",
                "Бензин",
                R.drawable.ic_mercedes_black
            ),
            Car(
                1,
                "CLA C118",
                "Mercedes-Benz",
                2800,
                "А/Т",
                "Бензин",
                R.drawable.ic_mercedes_black
            ),
            Car(2,
                "X7",
                "BMW",
                3000,
                "А/Т",
                "Бензин",
                R.drawable.ic_mercedes_black),
            Car(3,
                "Q8",
                "Audi", 2800,
                "А/Т",
                "Бензин",
                R.drawable.ic_audi_q8),
            Car(4,
                "RS6",
                "Audi", 3000,
                "A/T",
                "Бензин",
                R.drawable.ic_audi_rs6)
        )
        runBlocking {
            carDao.insert(*initialCars.toTypedArray())
        }

}