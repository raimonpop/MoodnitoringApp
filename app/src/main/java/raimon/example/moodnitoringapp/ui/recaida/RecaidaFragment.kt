package raimon.example.moodnitoringapp.ui.recaida

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import raimon.example.moodnitoringapp.Constants
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentRecaidaBinding
import raimon.example.moodnitoringapp.model.Notes
import raimon.example.moodnitoringapp.ui.recaida.database.DatabaseHelper


class RecaidaFragment : Fragment() {
    private var binding: FragmentRecaidaBinding? = null
    private lateinit var hipoAdapter: RecaidaAdapter
    private lateinit var depreAdapter: RecaidaAdapter
    private lateinit var database: DatabaseHelper

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DatabaseHelper(this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewHipo()
        setupRecyclerViewDepre()
        setupBottonHipo()
        setupBottonDepre()

    }

    override fun onStart() {
        super.onStart()
        getData()
    }


     fun getData() {
        val data = database.getAllNotes()
        data.forEach { note ->
            addNoteAuto(note)
        }
    }

    private fun setupBottonDepre() {
        binding?.let { binding ->
            binding.btnAddDepre.setOnClickListener {
                if(binding.etDepre.text.toString().isNotBlank()){
                    val note = Notes(text = binding.etDepre.text.toString(), hipo_depr = false)
                    note.id = database.inserNote(note)
                    if (note.id != Constants.ID_ERROR){
                        addNoteAuto(note)
                        binding.etDepre.text?.clear()

                    }
                }else{
                    binding.etDepre.error = "Campo requerido"
                }
            }
        }
    }

    private fun addNoteAuto(note: Notes) {
        if(note.hipo_depr){
            hipoAdapter.add(note)
        }else{
            depreAdapter.add(note)
        }
    }

    private fun setupBottonHipo() {
        binding?.let { binding ->
            binding.btnAddHipo.setOnClickListener {
                if(binding.etHipo.text.toString().isNotBlank()){
                    val note = Notes(text = binding.etHipo.text.toString(), hipo_depr = true)
                    note.id = database.inserNote(note)
                    if (note.id != Constants.ID_ERROR){
                        addNoteAuto(note)
                        binding.etHipo.text?.clear()
                    }
                }else{
                    binding.etHipo.error = "Campo requerido"
                }
            }
        }
    }

    private fun setupRecyclerViewDepre() {
        depreAdapter = RecaidaAdapter(mutableListOf())
        binding?.let {
            it.rvDepre.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@RecaidaFragment.depreAdapter
            }
        }
    }

    private fun setupRecyclerViewHipo() {
        hipoAdapter = RecaidaAdapter(mutableListOf())
        binding?.let {
            it.rvHipo.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@RecaidaFragment.hipoAdapter
            }
        }
    }
}