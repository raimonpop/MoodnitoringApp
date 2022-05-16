package raimon.example.moodnitoringapp.ui.registroDatos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import raimon.example.moodnitoringapp.databinding.FragmentRegistroBinding
import raimon.example.moodnitoringapp.model.RegViewModel
import raimon.example.moodnitoringapp.model.Registro
import java.time.LocalDate
import java.time.LocalDateTime


class RegistroFragment : Fragment() {

    private val TAG = "Log:RegFrag"

    // ViewBinding
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: RegViewModel by viewModels()

    // Firebase
    private val auth = Firebase.auth
    val currentUser = auth.currentUser

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
        setupActionBar()
        //region LIVE DATA
        /**
         * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         * LIVE DATA
         */
        // Observer del LiveData: Estado Animo
        val eaObserver = Observer<Int> { ea ->
            // Udapte UI
//            Log.i(TAG, "Ha cambiado desde ViewModel a $ea")
//            binding.sbEstadoAnimo.setProgress(ea,false)
            binding.tvCurEstadoAnimo.text = ea.toString()
        }
        // Observer del LiveData: Horas sueño
        val hsObserver = Observer<Int> { hs ->
            // Udapte UI
//            binding.sbHorasSon.setProgress(hs, false)
            binding.tvCurHorasSon.text = hs.toString()
        }
        // Observer del LiveData: Actividades Físicas
        val hafObserver = Observer<Int> { haf ->
            // Udapte UI
//            binding.sbFisicas.setProgress(ha,false)
            binding.tvCurFisicas.text = haf.toString()
        }

        // Observer del LiveData: Actividades Artisticas/Intelectual
        val haaObserver = Observer<Int> { haa ->
            // Udapte UI
//            binding.sbFisicas.setProgress(ha,false)
            binding.tvCurIntelectual.text = haa.toString()
        }

        // Observer del LiveData: Actividades Sociales
        val hasObserver = Observer<Int> { has ->
            // Udapte UI
//            binding.sbFisicas.setProgress(ha,false)
            binding.tvCurSociales.text = has.toString()
        }

        // Observe the LiveData
        viewModel.curEstAnim.observe(viewLifecycleOwner, eaObserver)
        viewModel.curHorasSon.observe(viewLifecycleOwner, hsObserver)
        viewModel.curHorasActFis.observe(viewLifecycleOwner, hafObserver)
        viewModel.curHorasActArt.observe(viewLifecycleOwner, haaObserver)
        viewModel.curHorasActSoc.observe(viewLifecycleOwner, hasObserver)
        /**
         * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         */
        //endregion

        //#region Estado Animo SeekBar
        binding.sbEstadoAnimo.min = 0
        binding.sbEstadoAnimo.max = 10
        binding.sbEstadoAnimo.progress = 5

        binding.sbEstadoAnimo.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            var curProgress: Int = 0

            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                // Actualizamos el atributo de clase
                curProgress = progress
                // Y la UI
//                binding.tvCurEstadoAnimo.text = progress.toString()
                viewModel.updateEA(curProgress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
//                Log.i(TAG,"Empieza a arrastrar...")
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
//                Log.i(TAG,"Ha soltado")
//                viewModel.updateEA(curProgress)
            }
        })
        //#endregion

        //#region Horas sueño SeekBar
        binding.sbHorasSon.min = 0
        binding.sbHorasSon.max = 24
        binding.sbHorasSon.progress = 8

        binding.sbHorasSon.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            var curProgress: Int = 0

            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                // Actualizamos el atributo de clase
                curProgress = progress
                // Y la UI
//                binding.tvCurEstadoAnimo.text = progress.toString()
                viewModel.updateHS(curProgress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
//                Log.i(TAG,"Empieza a arrastrar...")
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
//                Log.i(TAG,"Ha soltado")
//                viewModel.updateEA(curProgress)
            }
        })
        //#endregion

        //#region Horas Actividades Fisicas SeekBar
        binding.sbFisicas.min = 0
        binding.sbFisicas.max = 24
        binding.sbFisicas.progress = 3

        binding.sbFisicas.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            var curProgress: Int = 0

            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                // Actualizamos el atributo de clase
                curProgress = progress
                // Y la UI
//                binding.tvCurEstadoAnimo.text = progress.toString()
                viewModel.updateHAF(curProgress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
//                Log.i(TAG,"Empieza a arrastrar...")
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
//                Log.i(TAG,"Ha soltado")
//                viewModel.updateEA(curProgress)
            }
        })
        //#endregion

        //#region Horas Actividades Artisticas/Intelectual SeekBar
        binding.sbIntelectual.min = 0
        binding.sbIntelectual.max = 24
        binding.sbIntelectual.progress = 2

        binding.sbIntelectual.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            var curProgress: Int = 0

            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                // Actualizamos el atributo de clase
                curProgress = progress
                // Y la UI
//                binding.tvCurEstadoAnimo.text = progress.toString()
                viewModel.updateHAA(curProgress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
//                Log.i(TAG,"Empieza a arrastrar...")
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
//                Log.i(TAG,"Ha soltado")
//                viewModel.updateEA(curProgress)
            }
        })
        //#endregion

        //#region Horas Actividades Fisicas SeekBar
        binding.sbSocial.min = 0
        binding.sbSocial.max = 24
        binding.sbSocial.progress = 2

        binding.sbSocial.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            var curProgress: Int = 0

            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                // Actualizamos el atributo de clase
                curProgress = progress
                // Y la UI
//                binding.tvCurEstadoAnimo.text = progress.toString()
                viewModel.updateHAS(curProgress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
//                Log.i(TAG,"Empieza a arrastrar...")
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
//                Log.i(TAG,"Ha soltado")
//                viewModel.updateEA(curProgress)
            }
        })
        //#endregion


        //region Botones
        binding.btSave.setOnClickListener {
            // Si no hay nadie logado, abortamos misión.
            if (currentUser == null) return@setOnClickListener

            //val YYYY = LocalDateTime.now().year
            //val MM = LocalDateTime.now().monthValue
            //val DD = LocalDateTime.now().dayOfMonth
            val ea = binding.tvCurEstadoAnimo.text.toString().toInt()
            val hs = binding.tvCurHorasSon.text.toString().toInt()
            var ha = 0
            if (binding.cbFisicas.isChecked) ha += binding.tvCurFisicas.text.toString().toInt()
            if (binding.cbIntelectual.isChecked) ha += binding.tvCurIntelectual.text.toString()
                .toInt()
            if (binding.cbSocial.isChecked) ha += binding.tvCurSociales.text.toString().toInt()

            // Solo vamos a registrar datos consistentes horas < 24
            if (hs + ha > 24) {
//                Log.i(TAG, "error captured")
                Toast.makeText(
                    activity,
                    "Entre las horas de sueño y las actividades sobrepasas 24h..",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else {
                val reg = Registro(
                    usuario = auth.currentUser?.uid.toString(),
                    fecha = Timestamp.now() //"2022.01.02" // LocalDate.now().toString()
                    //, yyyy = YYYY
                    //, mm = MM
                    // , dd = DD
                    ,
                    estAnim = ea,
                    horasSon = hs,
                    horasAct = ha
                )
//              FirestoreDAONC.addRegister(reg1)
                viewModel.insertReg(reg)
            }
        }
        //endregion
    }
    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title = "Registrar datos"
            //setHasOptionsMenu(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}