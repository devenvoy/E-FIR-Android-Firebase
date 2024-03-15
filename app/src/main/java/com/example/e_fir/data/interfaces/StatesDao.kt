package com.example.e_fir.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.e_fir.data.modal.States

@Dao
interface StatesDao {

    @Query("select Distinct(State) from States")
    fun getAllStateData(): List<String>

    @Query("SELECT District from States where State = :stateName")
    fun getDistrictData(stateName: String): List<String>

    @Insert
    fun insertState(state: States)

}