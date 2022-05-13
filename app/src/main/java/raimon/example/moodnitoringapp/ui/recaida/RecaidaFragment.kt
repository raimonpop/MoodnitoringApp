package raimon.example.moodnitoringapp.ui.recaida

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentRecaidaBinding
import raimon.example.moodnitoringapp.ui.recaida.database.DatabaseHelper


class RecaidaFragment : Fragment() {
    private var binding: FragmentRecaidaBinding? = null
    private lateinit var database: DatabaseHelper
    private lateinit var hipoAdapter: RecaidaAdapter
    private lateinit var depreAdapter: RecaidaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecaidaBinding.inflate(inflater, container, false)
        binding?.let {
            return it.root
        }
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}