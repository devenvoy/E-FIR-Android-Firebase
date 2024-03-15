package com.example.e_fir.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Complaint(

    @PrimaryKey(autoGenerate = true)
    var C_ID: Int,
    var HEAD: String

)
