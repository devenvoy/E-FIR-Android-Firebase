package com.example.e_fir.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var ID: String,
    var NAME: String,
    var AGE: String,
    var NUMBER: String,
    var ADDRESS: String,
    var GENDER: String,
    var ADHARNUM: String,
    var PINCODE: String,
    var CITY: String,
)
