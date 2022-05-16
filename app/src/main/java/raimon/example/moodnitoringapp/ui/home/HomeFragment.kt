package raimon.example.moodnitoringapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
private val firebase = FirebaseAuth.getInstance()
    // ViewBinding
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // NavController
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_sign_out ->{
                AuthUI.getInstance().signOut(binding.root.context)
                    .addOnSuccessListener {
                        Toast.makeText(binding.root.context, "Sesion terminada.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        if (!it.isSuccessful) {
                            Toast.makeText(binding.root.context, "No se pudo cerrar la sesion.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

            }
            R.id.action_profile ->{
                navController.navigate(R.id.nav_profileFragment)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view) // Instancia el navController.
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.show()
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar?.title = "Buenas, "+firebase.currentUser?.displayName
            setHasOptionsMenu(true)
        }

        // NavController


        // Navegación
        binding.mbRegistro.setOnClickListener {
            navController.navigate(R.id.nav_registroFragment)
        }
        binding.mbDatos.setOnClickListener {
            navController.navigate(R.id.nav_visualizacionFragment)
        }
        binding.mbSeAles.setOnClickListener {
            navController.navigate(R.id.nav_recaidaFragment)
        }
        binding.mbActuacion.setOnClickListener {
            navController.navigate(R.id.nav_actuacionFragment)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Libero el _binding
        _binding = null
    }

    /**
     * Out of LifeCycle
     */
}