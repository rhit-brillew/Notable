package edu.rosehulman.notable

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import coil.load
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.notable.databinding.ActivityMainBinding
import edu.rosehulman.notable.models.Profile
import edu.rosehulman.notable.models.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController
    lateinit var authStateListener: FirebaseAuth.AuthStateListener

    lateinit var navView: NavigationView
    val signinLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { /* empty since the auth listener already responds .*/ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAuthListener()

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_notes, R.id.nav_guitars_list, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun initializeAuthListener(){
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            if(user==null){
                setupAuthUI()
            }else{
                //todo: logged in here. do we want users to do anything on account creation?
                with(user){
                    Log.d(Constants.TAG, "Profile: $uid, $email, $displayName")
                }
                val profileModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
                profileModel.getOrMakeProfile {
                    if(profileModel.hasCompletedSetup()){

                        var name: TextView = navView.getHeaderView(0).findViewById(R.id.nav_drawer_name)
                        var photo: ImageView = navView.getHeaderView(0).findViewById(R.id.nav_drawer_photo)
                        name.setText(profileModel.profile!!.name)
                        photo.load(profileModel.profile!!.storageURIString)

                        val id = findNavController(R.id.nav_host_fragment_content_main).currentDestination!!.id
                        if(id.toString() == R.id.nav_profile_loading.toString()){
                            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_notes)
                        }
                    }
                    else{
                        findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_profile_edit)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Firebase.auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }

    fun setupAuthUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setLogo(R.drawable.logo)
            .setTheme(R.style.Theme_Notable)
            .build()
        signinLauncher.launch(signinIntent)
    }
}