package com.example.e_fir.data.Singletons

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.e_fir.data.database.DatabaseHelper
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


object StatesDbHandler {

    fun getDb(context: Context): DatabaseHelper {
        copyAttachedDatabase(context,"ecopdb.db")
        return Room.databaseBuilder(context, DatabaseHelper::class.java, "ecopdb.db")
            .createFromAsset("assets/ecopdb.db")
            .allowMainThreadQueries().build()
    }

    private fun copyAttachedDatabase(context: Context, databaseName: String) {
        val dbPath = context.getDatabasePath(databaseName)

        // If the database already exists, return
        if (dbPath.exists()) {
            return
        }

        // Make sure we have a path to the file
        dbPath.getParentFile()?.mkdirs()

        // Try to copy database file
        try {
            val inputStream = context.assets.open("$databaseName")
            val output: OutputStream = FileOutputStream(dbPath)
            val buffer = ByteArray(8192)
            var length: Int
            while (inputStream.read(buffer, 0, 8192).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }
            output.flush()
            output.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d("====-===", "Failed to open file", e)
            e.printStackTrace()
        }
    }

}



