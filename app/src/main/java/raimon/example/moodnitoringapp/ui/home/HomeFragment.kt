package raimon.example.moodnitoringapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NavController
        navController = Navigation.findNavController(view) // Instancia el navController.

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