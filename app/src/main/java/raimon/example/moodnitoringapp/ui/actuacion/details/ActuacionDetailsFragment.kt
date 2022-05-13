package raimon.example.moodnitoringapp.ui.actuacion.details

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentActuacionDetailsBinding
import raimon.example.moodnitoringapp.model.Actuacion
import raimon.example.moodnitoringapp.ui.actuacion.ActuacionFragment

class ActuacionDetailsFragment(actuacionSelected: Actuacion) : BottomSheetDialogFragment() {
    private var binding: FragmentActuacionDetailsBinding? = null
    private var actuacion: Actuacion? = actuacionSelected

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentActuacionDetailsBinding.inflate(LayoutInflater.from(context))
        binding?.let {
            val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)

            bottomSheetBehavior = BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


            getActuacion()
            return bottomSheetDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun getActuacion(){
        actuacion?.let { actuacio ->
            binding?.let {
                it.tvDesciption.text = actuacio.description
                it.tvTitle.text = actuacio.title
            }
        }
    }
}