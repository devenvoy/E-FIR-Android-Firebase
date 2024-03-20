package com.example.e_fir.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.e_fir.R
import com.example.e_fir.data.adapter.MyNodeAdapter
import com.example.e_fir.data.modal.PageNode
import com.example.e_fir.data.modal.SubPageNode
import com.example.e_fir.databinding.ActivityHomePageBinding
import com.example.e_fir.ui.Activity.EmergencyNumActivity
import com.example.e_fir.ui.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomePage : AppCompatActivity() {

    var isExpanded = false

    lateinit var auth: FirebaseAuth

    private val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab)
    }

    private val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }


    val mainList = arrayOf(
        PageNode(
            "FIR", R.drawable.ic_fir_node, "",
            arrayListOf(
                SubPageNode("Register e-FIR", R.drawable.node1_1, "", "11"),
                SubPageNode("View/ Download FIR", R.drawable.node1_2, "", "12"),
                SubPageNode("Register Lost Article", R.drawable.node1_3, "", "13"),
                SubPageNode(
                    "View/Download Lost Article Report (LAR)",
                    R.drawable.node1_4,
                    "",
                    "14"
                ),
                SubPageNode("Road Accident Cases", R.drawable.node1_5, "", "15"),
            )
        ),

        PageNode(
            "Complaints", R.drawable.ic_complaint_node, "",
            arrayListOf(
                SubPageNode("Senior Citizen Complaint", R.drawable.node2_1, "", "21"),
                SubPageNode("Complaint By Divyang", R.drawable.node2_2, "", "22"),
                SubPageNode("Share Information", R.drawable.node2_3, "", "23"),
            )
        ),
        PageNode(
            "Permissions", R.drawable.ic_permission_node, "",
            arrayListOf(
                SubPageNode("Procession Request", R.drawable.node3_1, "", "31"),
                SubPageNode("Protest/Strike Request", R.drawable.node3_2, "", "32"),
                SubPageNode("Event Performance", R.drawable.node3_3, "", "33"),
                SubPageNode("Film Shooting", R.drawable.node3_4, "", "34"),
            )
        ),
        PageNode(
            "Service", R.drawable.ic_service_node, "",
            arrayListOf(
                SubPageNode(
                    "Character Certificate ( Paid Service -   50)",
                    R.drawable.node4_1,
                    "",
                    "41"
                ),
                SubPageNode(
                    "Domestic Help Verification(Paid Service -  50)",
                    R.drawable.node4_2,
                    "", "42"
                ),
                SubPageNode(
                    "Employee Verification( Paid Service -   50) ",
                    R.drawable.node4_3,
                    "",
                    "43"
                ),
                SubPageNode(
                    "Tenant Verification( Paid Service -   50)",
                    R.drawable.node4_4,
                    "",
                    "44"
                ),
                SubPageNode("Postmortem Report Request", R.drawable.node4_5, "", "45"),
            )
        ),
        PageNode(
            "Information", R.drawable.ic_information_node, "",
            arrayListOf(
                SubPageNode("Nearest Police Station", R.drawable.node5_1, "", "51"),
                SubPageNode("Stolen / Recovered Vehicle", R.drawable.node5_2, "", "52"),
                SubPageNode("Unidentified Dead Bodies", R.drawable.node5_3, "", "53"),
                SubPageNode("Missing Person ", R.drawable.node5_4, "", "54"),
                SubPageNode("Rewarded Criminals", R.drawable.node5_5, "", "55"),
                SubPageNode("Arrested Accused", R.drawable.node5_6, "", "56"),
                SubPageNode("Cyber Awareness", R.drawable.node5_7, "", "57"),
                SubPageNode("Get Vehicle NOC", R.drawable.node5_8, "", "58"),
            )
        ),
        PageNode(
            "Search Your Application\nStatus/Download", R.drawable.ic_etc1_node, "",
            arrayListOf()
        )
    )

    private val binding: ActivityHomePageBinding by lazy {
        ActivityHomePageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // on functionality onBackPressed
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        auth = Firebase.auth

        val myNodeAdapter = MyNodeAdapter(this@HomePage, mainList)
        binding.mainRecyclerView.adapter = myNodeAdapter

        val toggleButton =
            ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.open,
                R.string.close
            )
        toggleButton.drawerArrowDrawable.color = getResources().getColor(R.color.white)
        binding.drawerLayout.addDrawerListener(toggleButton)
        toggleButton.syncState()

        binding.mainFab.setOnClickListener {
            if (isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }

        binding.subFab.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:112")))
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Logout")
                    builder.setMessage("Do yo Sure Want To Logout?")

                    builder.setPositiveButton("Yes") { dialog, which ->
                        auth.signOut()
                        startActivity(Intent(this@HomePage, SplashScreen::class.java))
                        finish()
                    }

                    builder.setNegativeButton("No") { dialog, which ->

                    }
                    builder.show()
                }

                R.id.user_profile -> {
                    //TODO -do something
                }

                R.id.home -> {
                    //TODO -do something
                }

                R.id.emergencyNum -> {
                    startActivity(Intent(this@HomePage, EmergencyNumActivity::class.java))
                }

                R.id.unistallApps -> {
                    //TODO -do something
                }

            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    private fun expandFab() {
        isExpanded = !isExpanded

        binding.subFab.animate()
            .translationY(binding.subFab.height * -1.5f)
            .withStartAction {
                binding.subFab.visibility = View.VISIBLE
            }
            .setDuration(500)
/*//        binding.mainFab.startAnimation(rotateClockWiseAnim)
        binding.subFab.startAnimation(toBottomFabAnim)*/
    }

    private fun shrinkFab() {
        isExpanded = !isExpanded
        binding.subFab.animate()
            .translationY(0f)
            .withEndAction {
                binding.subFab.visibility = View.GONE
            }
            .setDuration(500)
        /*//        binding.mainFab.startAnimation(rotateAntiClockWiseAnim)
        binding.subFab.startAnimation(fromBottomFabAnim)
        binding.subFab.visibility = View.GONE*/
    }

    // New Method Instead of Using onBackPressed
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }
    }
}