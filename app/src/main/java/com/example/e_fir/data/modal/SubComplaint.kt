package com.example.e_fir.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubComplaint(

    @PrimaryKey(autoGenerate = true)
    var SC_ID: Int,
    var C_ID: Int,
    var MINOR_HEAD: String
)