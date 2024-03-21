package com.example.e_fir.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.e_fir.R
import com.example.e_fir.data.adapter.MyNodeAdapter
import com.example.e_fir.data.modal.PageNode
import com.example.e_fir.data.modal.SubPageNode
import com.example.e_fir.data.modal.User
import com.example.e_fir.databinding.ActivityHomePageBinding
import com.example.e_fir.ui.Activity.EmergencyNumActivity
import com.example.e_fir.ui.Activity.showProfileActivity
import com.example.e_fir.ui.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class HomePage : AppCompatActivity() {

    var isExpanded = false

    lateinit var sharedPref: SharedPreferences
    lateinit var editor: Editor
    lateinit var user: User

    // Firebase auth
    lateinit var auth: FirebaseAuth

    private lateinit var imageView: ImageView
    private lateinit var userNumber: TextView
    private lateinit var userName: TextView

    // page Category and sub category items list
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

    // this page binding
    private val binding: ActivityHomePageBinding by lazy {
        ActivityHomePageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // on functionality onBackPressed
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        // initialize auth
        auth = Firebase.auth

        sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        editor = sharedPref.edit()

        // main page category adapter
        val myNodeAdapter = MyNodeAdapter(this@HomePage, mainList)
        // set adapter
        binding.mainRecyclerView.adapter = myNodeAdapter

        // toggle action for drawer icon
        val toggleButton =
            ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.open,
                R.string.close
            )
        // toggle icon color
        toggleButton.drawerArrowDrawable.color = getResources().getColor(R.color.white)
        // add toggle button to action bar / tool bar
        binding.drawerLayout.addDrawerListener(toggleButton)
        // sync with navigationview
        toggleButton.syncState()

        // main Floating action button
        binding.mainFab.setOnClickListener {
            if (isExpanded) {
                shrinkFab()
            } else {
                expandFab()
            }
        }

        // action on sub floating action button
        binding.subFab.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:112")))
        }

        val headerView = binding.navView.getHeaderView(0)

        imageView = headerView.findViewById(R.id.dp)
        userName = headerView.findViewById(R.id.userName)
        userNumber = headerView.findViewById(R.id.user_number)


        val json = sharedPref.getString("user", null)
        user = Gson().fromJson(json, User::class.java)


        Glide.with(this).load(user.userDp).into(imageView)
        userName.text = user.NAME
        userNumber.text = user.NUMBER

        // Navigation view menu item selected
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
                    startActivity(Intent(this@HomePage, showProfileActivity::class.java))
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
            // close navigation drawer
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    // if close sub fab icon then open
    private fun expandFab() {
        isExpanded = !isExpanded
        binding.subFab.animate()
            .translationY(binding.subFab.height * -1.5f)
            .withStartAction {
                binding.subFab.visibility = View.VISIBLE
            }
            .setDuration(500)
    }

    // if open sub fab icon then close
    private fun shrinkFab() {
        isExpanded = !isExpanded
        binding.subFab.animate()
            .translationY(0f)
            .withEndAction {
                binding.subFab.visibility = View.GONE
            }
            .setDuration(500)
    }

    // New Method Instead of Using onBackPressed old deprecated function
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