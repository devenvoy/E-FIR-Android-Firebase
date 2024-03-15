package com.example.e_fir.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.e_fir.data.interfaces.StatesDao
import com.example.e_fir.data.modal.Complaint
import com.example.e_fir.data.modal.States
import com.example.e_fir.data.modal.SubComplaint

@Database(
    entities = [States::class, Complaint::class, SubComplaint::class],
    exportSchema = false,
    version = 2
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract val statesDao: StatesDao

}