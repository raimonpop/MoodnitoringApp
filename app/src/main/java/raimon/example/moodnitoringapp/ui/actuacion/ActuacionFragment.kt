package raimon.example.moodnitoringapp.ui.actuacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import raimon.example.moodnitoringapp.databinding.FragmentActuacionBinding
import raimon.example.moodnitoringapp.model.Actuacion
import raimon.example.moodnitoringapp.model.ActuacionData
import raimon.example.moodnitoringapp.ui.actuacion.details.ActuacionDetailsFragment

class ActuacionFragment : Fragment(), iActuacion{

    private var binding: FragmentActuacionBinding? = null
    private lateinit var adapter: ActuacionAdapter
    private val actuacionList = mutableListOf<Actuacion>()
    private var actuacionSelected: Actuacion?= null
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActuacionBinding.inflate(inflater, container, false)
        binding?.let {
            return it.root
        }
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupActionBar()
        navController = Navigation.findNavController(view)
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title = "Plan de actuacion"
            setHasOptionsMenu(false)
        }
    }

    private fun setupRecyclerView() {
       adapter = ActuacionAdapter(mutableListOf(), this)
       binding?.let {
           it.recyclesView.apply {
               layoutManager = LinearLayoutManager(context)
               adapter = this@ActuacionFragment.adapter
           }
       }
    }

    override fun openActuacion(actuacion: Actuacion) {
        val index = actuacionList.indexOf(actuacion)

        if(index != -1){
            actuacionSelected = actuacionList[index]
        }else{
            actuacionSelected = actuacion
        }

        val fragment = ActuacionDetailsFragment(actuacionSelected!!)
        fragment.show(requireActivity().supportFragmentManager, actuacion.title)

    }



    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData() {
        val data = ActuacionData.actuacionDatos
        data.forEach {
            adapter.add(it)
        }
    }


}