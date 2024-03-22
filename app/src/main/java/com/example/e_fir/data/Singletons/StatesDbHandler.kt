package com.example.e_fir.data.Singletons

import android.content.Context
import androidx.room.Room
import com.example.e_fir.data.database.DatabaseHelper

object StatesDbHandler {

    fun getDb(context: Context): DatabaseHelper {
        return Room.databaseBuilder(context, DatabaseHelper::class.java, "ecopdb.db")
            .createFromAsset("assets/database/ecopdb.db")
            .allowMainThreadQueries().build()
    }
}



