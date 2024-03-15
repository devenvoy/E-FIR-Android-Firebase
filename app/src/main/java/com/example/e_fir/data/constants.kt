package com.example.e_fir.data

import com.example.e_fir.data.modal.Complaint

class constants {

    companion object {

        var stateList: List<String> = listOf()
        var districtList = listOf<String>()

        var complaintList = listOf<Complaint>()
        var subComplaintList = listOf<String>()


        val stateList2 = arrayOf(
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal",
        )



        val gujratDistrict =
            arrayOf("Surat", "Rajkot", "Bharuch", "jamnagar", "katchh", "ahmdabad", "gandhinagar")

        val suratpolicestnList = arrayOf("p1", "p2", "p3", "p4", "p5", "p6")
        val complaintList2 = arrayOf("c1", "c2", "c3", "c4", "c5", "c6")
        val subComplaintList2 = arrayOf("sc1", "sc2", "sc3", "sc4", "sc5", "sc6")
    }
}