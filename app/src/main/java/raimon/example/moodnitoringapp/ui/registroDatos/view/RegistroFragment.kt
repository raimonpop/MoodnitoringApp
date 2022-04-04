package raimon.example.moodnitoringapp.ui.registroDatos.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.dao.FirestoreDAONC
import raimon.example.moodnitoringapp.databinding.FragmentRegistroBinding
import raimon.example.moodnitoringapp.databinding.FragmentVisualizacionBinding
import raimon.example.moodnitoringapp.model.Registro


class RegistroFragment : Fragment() {


    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_registro, container, false)
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Borrar
        binding.btSave.setOnClickListener {
            val reg1 = Registro(
                usuario = "carlos@test.com"
                , fecha = "2022.01.02"
                , estAnim = 8
                , horasSon = 10
                , horasAct = 15
            )
            FirestoreDAONC.addRegister(reg1)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}