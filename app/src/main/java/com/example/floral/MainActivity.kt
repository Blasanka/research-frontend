package com.example.floral


import SifaaAdapters.SifaaFloralFavAdapter
import SifaaAdapters.SifaaFloralItemAdapter
import SifaaAdapters.SifaaRecItemAdapter
import android.app.ActivityOptions
import android.app.AlertDialog

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import SifaaDataModels.SifaaCartItem
import SifaaDataModels.SifaaFloralItem
import de.hdodenhof.circleimageview.CircleImageView
import interfaces.FloralApi
import interfaces.RequestType
import kotlinx.android.synthetic.main.activity_main.*
import SifaaServices.DatabaseHandler
import SifaaServices.SifaaFirebaseDBService
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.testfloral.RetrofitInstance
import com.example.testfloral.Survey
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_survey.*
import kotlinx.android.synthetic.main.sifaa_activity_demand.*
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception


class MainActivity : AppCompatActivity(), SifaaRecItemAdapter.OnItemClickListener,SifaaFloralItemAdapter.OnItemClickListener, FloralApi {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    val TAG = "SIFAA"

    private val db = DatabaseHandler(this)

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout


    private var allItems = ArrayList<SifaaFloralItem>()
    private var allItems2 = ArrayList<SifaaFloralItem>()
    private var allItems3 = ArrayList<SifaaFloralItem>()
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var itemRecyclerView2: RecyclerView
    private lateinit var itemRecyclerView3: RecyclerView
    private lateinit var sifaaFloralAdapter: SifaaFloralItemAdapter
    private lateinit var sifaaRecAdapter: SifaaRecItemAdapter
    private lateinit var sifaaFavAdapter: SifaaFloralFavAdapter
    private var count = 0

    private lateinit var userIcon: CircleImageView
    private lateinit var showAllSwitch: SwitchCompat

    private lateinit var topHeaderLL: LinearLayout
    private lateinit var topSearchLL: LinearLayout
    private lateinit var searchBox: SearchView
    private lateinit var FlowerCategoryCV: CardView
    private lateinit var showAllLL: LinearLayout

    private var empName = ""
    private var empEmail = ""
    private var empGender = "male"
    private var mmId = ""
    private var recItemValidatedName = ""
    private var zAxes = ArrayList<Int>()
    private var xAxes = ArrayList<Int>()
    private var aAxes = ArrayList<Int>()
    private var bAxes = ArrayList<Int>()
    private var cAxes = ArrayList<Int>()
    private var dAxes = ArrayList<Int>()
    private var eAxes = ArrayList<Int>()
    private var fAxes = ArrayList<Int>()
    private var gAxes = ArrayList<Int>()


    private var searchIsActive = false
    private var doubleBackToExit = false
    private lateinit var surveyList: ArrayList<Survey>
    var seekBarNormal: SeekBar? = null
    private lateinit var sharedPref: SharedPreferences
    //val languages = resources.getStringArray(R.array.Languages)



    private lateinit var progressDialog: ProgressDialog

    override fun onBackPressed() {
        if (searchIsActive) {
            //un-hiding all the views which are above the items
            sifaaFloralAdapter.filter.filter("")
           // sifaaRecAdapter.filter.filter("")
            topHeaderLL.visibility = ViewGroup.VISIBLE
            FlowerCategoryCV.visibility = ViewGroup.VISIBLE
            showAllLL.visibility = ViewGroup.VISIBLE
            topSearchLL.visibility = ViewGroup.GONE
            searchIsActive = false
            return
        }
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        doubleBackToExit = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExit = false }, 2000)
    }

    //val languages = resources.getStringArray(R.array.Languages)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val strPassword: String = SifaaUserProfileActivity.eidd
//        Log.e(strPassword, "gdgergeg")






        sharedPref = getSharedPreferences("user_profile_details", MODE_PRIVATE)
        loadUserProfile()

       spinnerValidator()
        tvRecMin.isVisible = false

        btnSurvey.isVisible = false

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        db.clearCartTable()
        loadProfile()
        loadNavigationDrawer()
        loadMenu()
        //loadMenu2()
        loadSearchTask()

        userIcon = findViewById(R.id.menu_user_icon)
        userIcon.setOnClickListener {
            openUserProfileActivity()
        }

        //val languages = resources.getStringArray(R.array.Languages)


    }
    private fun loadUserProfile() {

        if(sharedPref.getString("emp_name", "none") != "none") {
            //it will load the details from offline, so it doesn't need to connect with online database, it will be fast
            loadOfflineUserProfile()
        } else {
            //if not saved, then save it offline
            val editor = sharedPref.edit()

            val user = FirebaseAuth.getInstance().currentUser!!

            editor.putString("emp_name", user.displayName!!)
            editor.putString("emp_email", user.email!!)

            val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            databaseRef.child("employees")
                .child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        editor.putString("emp_id_no", snapshot.child("emp_id").value.toString())
                        editor.putString("emp_mobile_no", snapshot.child("mobile_no").value.toString())
                        editor.putString("emp_org", snapshot.child("organization").value.toString())
                        editor.putString("emp_reg_id", snapshot.child("reg_id").value.toString())
                        editor.putString("emp_reg_date", snapshot.child("reg_date").value.toString())
                        editor.putString("emp_id_card_uri", snapshot.child("emp_id_card_uri").child("imageUrl").value.toString())

                        editor.apply()
                        loadOfflineUserProfile() //now load offline, because it is saved
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }
    private fun loadOfflineUserProfile() {
//        findViewById<TextView>(R.id.tvProfileTopName).text = sharedPref.getString("emp_name", "11")
//        findViewById<TextView>(R.id.tvProfileTopEmail).text = sharedPref.getString("emp_email", "11")
            mmId = sharedPref.getString("emp_id_no", "11").toString()
//        findViewById<TextView>(R.id.tvProfileMobileNo).text = sharedPref.getString("emp_mobile_no", "11")
//            mmId = sharedPref.getString("emp_org", "11").toString()
        Log.e(mmId, "HttpException, unexpected response")
//        findViewById<TextView>(R.id.tvProfileRegId).text = sharedPref.getString("emp_reg_id", "11")
//        findViewById<TextView>(R.id.tvProfileRegDate).text = sharedPref.getString("emp_reg_date", "11")
//
//        val empIdCardUri = sharedPref.getString("emp_id_card_uri", "")
//        try { // loading ID Card
//            Picasso.get().load(empIdCardUri).into(findViewById<ImageView>(R.id.profile_id_card_iv))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    private fun spinnerValidator() {
        val languages = resources.getStringArray(R.array.Languages)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if(languages[position] == "Show all"){
                                sifaaFloralAdapter.filterList(allItems) //display complete list
                                sifaaRecAdapter.filterList(allItems2) //display complete list
                                sifaaFavAdapter.filterList(allItems3) //display complete list
                                val container: LinearLayout = findViewById(R.id.flower_categories_container_ll)
                                for (ll in container.children) {
                                    ll.alpha =
                                        1.0f
                                }
                    }


                    if(languages[position] == "Sort by price"){
                        //sifaaFloralAdapter.filterList(allItems) //display complete list
                        //  sifaaRecAdapter.filterList(allItems2) //display complete list

                        //val languages = resources.getStringArray(R.array.Languages)

                        val container: LinearLayout = findViewById(R.id.flower_categories_container_ll)
                        for (ll in container.children) {
                            ll.alpha =
                                1.0f
                        }
                        val filterList = ArrayList<SifaaFloralItem>()
                        for (item in allItems) {
                            filterList.add(item)
                            filterList.sortBy { list -> list.itemPrice }
                        }
                       sifaaFloralAdapter.filterList(filterList)
                    }

                    if(languages[position] == "Sort by ratings"){
                        //sifaaFloralAdapter.filterList(allItems) //display complete list
                        //  sifaaRecAdapter.filterList(allItems2) //display complete list
                        val container: LinearLayout = findViewById(R.id.flower_categories_container_ll)
                        for (ll in container.children) {
                            ll.alpha =
                                1.0f
                        }
                        val filterList = ArrayList<SifaaFloralItem>()
                        for (item in allItems) {
                            filterList.add(item)
                            filterList.sortBy { list -> list.itemStars }
                        }
                        sifaaFloralAdapter.filterList(filterList)
                    }


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun loadProfile() {
        val user = auth.currentUser!!
        this.empName = user.displayName!!
        //val languages = resources.getStringArray(R.array.Languages)
        this.empEmail = user.email!!
        findViewById<TextView>(R.id.top_wish_name_tv).text = "Hi ${this.empName.split(" ")[0]}"
        Handler().postDelayed({
            findViewById<TextView>(R.id.nav_header_user_name).text = this.empName
        }, 1000)

        databaseRef.child("employees")
            .child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    empGender = snapshot.child("gender").value.toString()
                    //by default male icon is attached
                    if (empGender == "female") {
                        userIcon.setImageDrawable(resources.getDrawable(R.drawable.user_famale))
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun loadMenu() {
        val sharedPref: SharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        itemRecyclerView = findViewById(R.id.items_recycler_view)
        itemRecyclerView2 = findViewById(R.id.items_recycler_view2)
        itemRecyclerView3 = findViewById(R.id.items_recycler_view3)
        sifaaFloralAdapter = SifaaFloralItemAdapter(
            applicationContext,
            allItems,
            sharedPref.getInt("loadItemImages", 0),
            this
        )

        sifaaRecAdapter = SifaaRecItemAdapter(
            applicationContext,
            allItems2,
            sharedPref.getInt("loadItemImages", 0),
            this
        )

        sifaaFavAdapter = SifaaFloralFavAdapter(
            applicationContext,
            allItems3,
            sharedPref.getInt("loadItemImages", 0),
            this
        )

        itemRecyclerView.adapter = sifaaFloralAdapter
        itemRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        itemRecyclerView3.adapter = sifaaFavAdapter
        itemRecyclerView3.layoutManager = LinearLayoutManager(this@MainActivity)


        itemRecyclerView2.adapter = sifaaRecAdapter
        itemRecyclerView2.layoutManager = LinearLayoutManager(this@MainActivity)



        when (sharedPref.getInt("menuMode", 0)) {
            0 -> loadOnlineMenu()
            1 -> { // Offline
                val data = db.readOfflineMenuData()

                if (data.size == 0) {
                    AlertDialog.Builder(this)
                        .setMessage("Offline Menu is now not available. Do you want download the menu for Offline?")
                        .setPositiveButton(
                            "Yes, Download it",
                            DialogInterface.OnClickListener { dialogInterface, _ ->
                                updateOfflineFlowerShop(true)
                                dialogInterface.dismiss()
                            })
                        .setNegativeButton(
                            "No, Continue to Online Mode",
                            DialogInterface.OnClickListener { dialogInterface, _ ->
                                loadOnlineMenu()
                                dialogInterface.dismiss()
                            })
                        .setCancelable(false)
                        .create().show()
                    return
                } else {
                    loadOfflineMenu()
                }
            }
        }
    }

    private fun loadOfflineMenu() {
        val data = db.readOfflineMenuData()

        for (i in 0 until data.size) {
            val item = SifaaFloralItem()
            item.itemID = data[i].itemID
            item.imageUrl = data[i].imageUrl
            item.itemName = data[i].itemName
            item.itemPrice = data[i].itemPrice
            item.itemShortDesc = data[i].itemShortDesc
            item.itemTag = data[i].itemTag
            item.itemStars = data[i].itemStars
            allItems.add(item)

        }
        sifaaFloralAdapter.notifyItemRangeInserted(0, allItems.size)
        sifaaRecAdapter.notifyItemRangeInserted(0, allItems2.size)
        sifaaFavAdapter.notifyItemRangeInserted(0, allItems3.size)
    }

    private fun loadOnlineMenu() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Loading...")
        //progressDialog.setMessage("For fast and smooth experience, you can download Menu for Offline.")
        progressDialog.setMessage("Please wait...")
        progressDialog.create()
        progressDialog.show()

        SifaaFirebaseDBService().readAllMenu(this, RequestType.READ)
    }

    private fun loadSearchTask() {
        topHeaderLL = findViewById(R.id.main_activity_top_header_ll)
        topSearchLL = findViewById(R.id.main_activity_top_search_ll)
        searchBox = findViewById(R.id.search_menu_items)
        FlowerCategoryCV = findViewById(R.id.main_activity_flower_categories_cv)
        showAllLL = findViewById(R.id.main_activity_show_all_ll)

        findViewById<ImageView>(R.id.main_activity_search_iv).setOnClickListener {
            //val languages = resources.getStringArray(R.array.Languages)
            topHeaderLL.visibility = ViewGroup.GONE
            showAllLL.visibility = ViewGroup.GONE
            topSearchLL.visibility = ViewGroup.VISIBLE
            FlowerCategoryCV.visibility = ViewGroup.GONE
            sifaaFloralAdapter.filterList(allItems)
            sifaaRecAdapter.filterList(allItems2)
            sifaaFavAdapter.filterList(allItems3)
            searchIsActive = true
        }

        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                sifaaFloralAdapter.filter.filter(p0)
                return false
            }
        })
    }

    fun showTagItems(view: View) {
        //displays the items which are of same category
        val container: LinearLayout = findViewById(R.id.flower_categories_container_ll)
        for (ll in container.children) {
            ll.alpha = 1.0f
        }
        (view as LinearLayout).alpha = 0.5f
        val tag = ((view as LinearLayout).getChildAt(1) as TextView).text.toString()
        val filterList = ArrayList<SifaaFloralItem>()
        val filterList2 = ArrayList<SifaaFloralItem>()
        val filterList3 = ArrayList<SifaaFloralItem>()
        for (item in allItems) {
            if (item.itemTag == tag) filterList.add(item)
        }

        for (item in allItems2) {
            if (item.itemTag == tag) filterList2.add(item)
        }

        for (item in allItems3) {
                if (item.itemTag == tag) filterList3.add(item)
            }

        sifaaFloralAdapter.filterList(filterList)
        sifaaRecAdapter.filterList(filterList2)
        sifaaFavAdapter.filterList(filterList3)
//        sp.isChecked = false
        spinner.setSelection(3)
    }

    private fun loadNavigationDrawer() {
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawerDelay: Long = 150 //delay of the drawer to close
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_flower_shop -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.detector -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({ navigateToDetectorActivity() }, drawerDelay)
                }
                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({ openUserProfileActivity() }, drawerDelay)
                }
                R.id.nav_my_orders -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                SifaaMyCurrentOrdersActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_orders_history -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                SifaaOrdersHistoryActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_share_app -> {
                    shareApp()
                }

                R.id.nav_contact_us -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                SifaaContactActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_update_shop -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    updateOfflineFlowerShop()
                }
                R.id.nav_settings -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                SifaaSettingsActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }
                R.id.nav_log_out -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    logOutUser()
                }
                R.id.dForecast -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                DemandActivity::class.java
                            )
                        )
                    }, drawerDelay)
                }

                R.id.nav_purchased_history -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(
                            Intent(
                                this,
                                SifaaPrevious::class.java
                            )
                        )
                    }, drawerDelay)
                }
            }
            true
        }

        findViewById<ImageView>(R.id.nav_drawer_opener_iv).setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun logOutUser() {


        android.app.AlertDialog.Builder(this)
            .setTitle("Attention")
            .setMessage("Are you sure you want to Log Out ? You will lose all your SIFAA Orders, as it is a beta App")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                Firebase.auth.signOut()

                getSharedPreferences("settings", MODE_PRIVATE).edit().clear()
                    .apply() //deleting settings from offline
                getSharedPreferences("user_profile_details", MODE_PRIVATE).edit().clear()
                    .apply() //deleting user details from offline

                //removing tables
                db.dropCurrentOrdersTable()
                db.dropOrderHistoryTable()
                db.clearSavedCards()

                startActivity(Intent(this, SIfaaUserLoginActivity::class.java))
                finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            )
            .create().show()
    }

    private fun shareApp() {
        val message =
            "Try out this awesome App on Google Play!\nhttps://play.google.com/store/apps/details?id=$packageName"
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share To"))
    }

    fun showSifaaBottomCartDialog(view: View) {
        val bottomDialog = SifaaDialogBottomSheetItem()
        val bundle = Bundle()

        var totalPrice = 0.0f
        var totalItems = 0

        for (item in db.readCartData()) {
            totalPrice += item.itemPrice
            totalItems += item.quantity
        }

        bundle.putFloat("totalPrice", totalPrice)
        bundle.putInt("totalItems", totalItems)
        // bundle.putParcelableArrayList("orderedList", recyclerFloricultureAdapter.getOrderedList() as ArrayList<out Parcelable?>?)

        bottomDialog.arguments = bundle
        bottomDialog.show(supportFragmentManager, "BottomSheetDialog")
    }
    fun btnTakeSurvey(i: Int) {
//


    }

    private fun navigateToDetectorActivity() {
        val diseaseFragment = DetectorFragment()
        supportFragmentManager.beginTransaction().add(R.id.diseaseDetectorLayout, diseaseFragment)
            .commit()
    }

    private fun openUserProfileActivity() {
        val intent = Intent(this, SifaaUserProfileActivity::class.java)
        intent.putExtra("gender", this.empGender)

        val options =
            ActivityOptions.makeSceneTransitionAnimation(this, userIcon, "userIconTransition")
        startActivity(intent, options.toBundle())
    }

    private fun updateOfflineFlowerShop(offlineMenuToVisible: Boolean = false) {
        db.clearTheOfflineShopTable() // clear the older records first

        progressDialog.setTitle("Updating...")
        progressDialog.setMessage("Offline Menu is preparing for you...")
        progressDialog.show()

        SifaaFirebaseDBService().readAllMenu(this, RequestType.OFFLINE_UPDATE)
    }

    override fun onFetchSuccessListener(list: ArrayList<SifaaFloralItem>, requestType: RequestType) {
        val filterList = ArrayList<SifaaFloralItem>()

        if (requestType == RequestType.READ) {
            for (item in list) {
                allItems.add(item)
            }
            sifaaFloralAdapter.notifyItemRangeInserted(0, allItems.size)
        }
//        seekBarNormal = findViewById(R.id.btntest2)
//        seekBarNormal?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(
//                seekBar: SeekBar, progress: Int,
//                fromUser: Boolean) {
//
//                when (progress) {
//                    20 -> {
//                        for (item in allItems) {
//                            if (item.itemStars == 1) filterList.add(item)
//                        }
//                        sifaaFloralAdapter.filterList(filterList)
//                    }
//
//                }
//
//            }
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//                Toast.makeText(applicationContext, "seekbar touch started!", Toast.LENGTH_SHORT).show() }
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//                Toast.makeText(applicationContext, "seekbar touch stopped!", Toast.LENGTH_SHORT).show() }
//        })

//        if (requestType == RequestType.READ) {
//            for (item in list) {
//                    allItems.add(item)
//            }
//
//            sifaaFloralAdapter.notifyItemRangeInserted(0, allItems.size)
//        }

        lifecycleScope.launchWhenCreated {
            progressBar.isVisible = true

            //  progressBar2.isVisible = true
            val response = try {
                RetrofitInstance.api.getRec(mmId)

            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                progressBar.isVisible = false
                //     progressBar2.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                progressBar.isVisible = false
                //     progressBar2.isVisible = false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                progressBar.isVisible = false
                //     progressBar2.isVisible = false
                tvRecMin.isVisible = true
                if (requestType == RequestType.READ) {
                    for (item in list) {
                        for (i in response.body()!!.indices) {
                            zAxes.add(response.body()!![i].id)
                            if (item.itemID == zAxes[i].toString()) {
                                    allItems2.add(item)
                                // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                            }
                        }
                        sifaaRecAdapter.notifyItemRangeInserted(0, allItems2.size)
                    }
                }
                    if(allItems2.isEmpty()){
                        items_recycler_view2.isVisible = false

                        rb01.isVisible = true
                        rb02.isVisible = true
                        rb03.isVisible = true
                        rb04.isVisible = true
                        rb05.isVisible = true
                        rb06.isVisible = true
                        rb07.isVisible = true
                        rb08.isVisible = true


                        rb01.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                xAxes.add(response.body()!![i].id)
                                                if (item.itemID == xAxes[i].toString()) {
                                                        allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(0, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }

                        rb02.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                aAxes.add(response.body()!![i].id)
                                                if (item.itemID == aAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(xAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }

                        rb03.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                bAxes.add(response.body()!![i].id)
                                                if (item.itemID == bAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(aAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }

                        rb04.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                cAxes.add(response.body()!![i].id)
                                                if (item.itemID == cAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(bAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }


                        rb05.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                dAxes.add(response.body()!![i].id)
                                                if (item.itemID == dAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(cAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }


                        rb06.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                eAxes.add(response.body()!![i].id)
                                                if (item.itemID == eAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(dAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }


                        rb07.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                fAxes.add(response.body()!![i].id)
                                                if (item.itemID == fAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(eAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }


                        rb08.setOnCheckedChangeListener { _, isChecked ->

                            lifecycleScope.launchWhenCreated {


                                //  progressBar2.isVisible = true
                                val response = try {
                                    RetrofitInstance.api.getRec(mmId)

                                } catch (e: IOException) {
                                    Log.e(TAG, "IOException, you might not have internet connection")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                } catch (e: HttpException) {
                                    Log.e(TAG, "HttpException, unexpected response")

                                    //     progressBar2.isVisible = false
                                    return@launchWhenCreated
                                }
                                if (response.isSuccessful && response.body() != null) {
                                    items_recycler_view3.isVisible = true
                                    if (requestType == RequestType.READ) {
                                        for (item in list) {
                                            for (i in response.body()!!.indices) {
                                                gAxes.add(response.body()!![i].id)
                                                if (item.itemID == gAxes[i].toString()) {
                                                    allItems3.add(item)
                                                    // Log.e(response.body()!![0].name, "HttpException, unexpected response")
                                                }
                                            }
                                            sifaaFavAdapter.notifyItemRangeInserted(fAxes.size, allItems3.size)
                                        }
                                    }
                                }
                            }
                        }
                    }

            }
        }



        if (requestType == RequestType.OFFLINE_UPDATE) {
            for (item in list) {
                db.insertOfflineShopData(item)
            }
            Toast.makeText(applicationContext, "Offline Menu Updated", Toast.LENGTH_LONG).show()
            loadOfflineMenu()
        }

        progressDialog.dismiss()
    }

    override fun onItemClick(itemSifaa: SifaaFloralItem) {


        lifecycleScope.launchWhenCreated {
         //   progressBar.isVisible = true

            //  progressBar2.isVisible = true
            val response = try {

                RetrofitInstance.api.getwri("${empName}::${empName}")

            } catch(e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                progressBar.isVisible = false
                //     progressBar2.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                progressBar.isVisible = false
                //     progressBar2.isVisible = false
                return@launchWhenCreated }
            if(response.isSuccessful && response.body() != null) {
                progressBar.isVisible = false
                //     progressBar2.isVisible = false
             //   tvRecMin.isVisible = true

                Toast.makeText(applicationContext, "Offline Menu Updated", Toast.LENGTH_LONG).show()
                //  todoAdapter.todos = response.body()!!
                //Log.e(response.body()!![0].name, "HttpException, unexpected response")
                //response.body()!![0].name = recItemValidatedName


                Log.e(zAxes[0].toString(), "expected response")

            } else {
                Log.e("hi", "HttpException, unexpectedasdasdasd response")
                //   Log.e(TAG, "Response not successful")
            }
            // binding.progressBar.isVisible = false


        }
    }

    override fun onPlusBtnClick(itemSifaa: SifaaFloralItem) {
        itemSifaa.quantity += 1
        db.insertCartItem(
            SifaaCartItem(
                itemID = itemSifaa.itemID,
                itemName = itemSifaa.itemName,
                imageUrl = itemSifaa.imageUrl,
                itemPrice = itemSifaa.itemPrice,
                quantity = itemSifaa.quantity,
                itemStars = itemSifaa.itemStars,
                itemShortDesc = itemSifaa.itemShortDesc
            )
        )
    }

    override fun onMinusBtnClick(itemSifaa: SifaaFloralItem) {
        if (itemSifaa.quantity == 0) return
        itemSifaa.quantity -= 1

        val cartItem = SifaaCartItem(
            itemID = itemSifaa.itemID,
            itemName = itemSifaa.itemName,
            imageUrl = itemSifaa.imageUrl,
            itemPrice = itemSifaa.itemPrice,
            quantity = itemSifaa.quantity,
            itemStars = itemSifaa.itemStars,
            itemShortDesc = itemSifaa.itemShortDesc
        )

        if (itemSifaa.quantity == 0) {
            db.deleteCartItem(cartItem)
        } else {
            db.insertCartItem(cartItem) // Update
        }
    }




}
