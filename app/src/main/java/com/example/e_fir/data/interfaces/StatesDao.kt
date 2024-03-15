package com.example.e_fir.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.e_fir.data.modal.Complaint
import com.example.e_fir.data.modal.States

@Dao
interface StatesDao {

    @Query("select Distinct(State) from States")
    fun getAllStateData(): List<String>

    @Query("SELECT District from States where State = :stateName")
    fun getDistrictData(stateName: String): List<String>

    @Insert
    fun insertState(state: States)


    @Query("Select MINOR_HEAD from SubComplaint where C_ID = :c_id")
    fun getsubComplaints(c_id: Int): List<String>

    @Query("select * from Complaint")
    fun getComplaints(): List<Complaint>

}