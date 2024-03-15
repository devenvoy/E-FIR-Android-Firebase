package com.example.e_fir.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_fir.data.interfaces.StatesDao
import com.example.e_fir.data.modal.States

@Database(entities = [States::class], exportSchema = false, version = 1)
abstract class DatabaseHelper : RoomDatabase() {

    abstract val statesDao : StatesDao

}