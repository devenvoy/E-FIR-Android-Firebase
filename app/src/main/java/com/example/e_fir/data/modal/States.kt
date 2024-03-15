package com.example.e_fir.data.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class States(

    @PrimaryKey(autoGenerate = true)
    var no: Int,
    @ColumnInfo(name = "District")
    var district: String,
    @ColumnInfo(name = "State")
    var state: String
)
