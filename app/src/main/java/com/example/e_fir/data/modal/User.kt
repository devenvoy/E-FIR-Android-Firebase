package com.example.e_fir.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var ID: String,
    var NAME: String,
    var NUMBER: String,
    var EMAIL: String,
    var GENDER: String,
    var AGE: String,
    var CITIZENSHIP: String,
    var COUNTRY: String,
    var STATE: String,
    var PINCODE: String,
    var ADDRESS: String,
    var userDp: String,
    var IDPROOFTYPE: String,
    var IDPROOFNUM: String,
    var filled: Boolean,
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "", "", "", filled = false)
}

