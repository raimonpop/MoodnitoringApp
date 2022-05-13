package raimon.example.moodnitoringapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import raimon.example.moodnitoringapp.databinding.ActivityMainBinding
import raimon.example.moodnitoringapp.databinding.FragmentActuacionDetailsBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    private val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val response = IdpResponse.fromResultIntent(it.data)

        if(it.resultCode == RESULT_OK){
            val user = FirebaseAuth.getInstance().currentUser
            if(user != null){
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            }else {
                if (response == null){
                    Toast.makeText(this,"Hasta luego", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    response.error?.let {
                        if(it.errorCode == ErrorCodes.NO_NETWORK){
                            Toast.makeText(this,"No hay conexion a internet", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,"Codigo de eroor: ${it.errorCode}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }


        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureAuth()
    }

    private fun configureAuth() {

        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            if (auth.currentUser != null){
                supportActionBar?.hide()
                //supportActionBar?.title = auth.currentUser?.displayName
            }else{
                val provider = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    //AuthUI.IdpConfig.GoogleBuilder().build(),
                    //AuthUI.IdpConfig.FacebookBuilder().build(),
                    //AuthUI.IdpConfig.PhoneBuilder().build()
                )

                resultLauncher.launch(
                    AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(provider)
                    .build())


            }
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAuth.addAuthStateListener(authStateListener)

    }

    override fun onPause() {
        super.onPause()
        firebaseAuth.removeAuthStateListener(authStateListener)

    }
}