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
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "https://firebasestorage.googleapis.com/v0/b/e-fir-434f7.appspot.com/o/Users%2Fuser.jpeg?alt=media&token=31579f13-6e85-49e1-85d2-035c7c4965f4", "", "", filled = false)
}

