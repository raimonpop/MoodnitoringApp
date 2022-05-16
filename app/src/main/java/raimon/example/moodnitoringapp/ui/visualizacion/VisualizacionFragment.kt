package raimon.example.moodnitoringapp.ui.visualizacion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.google.firebase.auth.FirebaseAuth
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentVisualizacionBinding
import raimon.example.moodnitoringapp.model.Grafico
import raimon.example.moodnitoringapp.model.Registro
import raimon.example.moodnitoringapp.model.VisViewModel
import java.util.ArrayList


class VisualizacionFragment : Fragment() {

    //    private lateinit var binding: FragmentVisualizacionBinding
    private val TAG = "Log:VisFrag"

    // ViewBinding
    private var _binding: FragmentVisualizacionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // ViewModel
    private val viewModel: VisViewModel by viewModels()

    // LineCharts
    private lateinit var lineChart: LineChart

    // Dias
    private var curDays: Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_visualizacion, container, false)
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentVisualizacionBinding.inflate(inflater, container, false)

        // Enlace LineChart con la vista
        lineChart = binding.lcMainChart

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()

        /**
         * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         * LIVE DATA
         */
        // Observer del LiveData
        val registrosObserver = Observer<ArrayList<Registro>> { registros ->
            // Update the UI with the new Data
            val grafico = Grafico(
                dias = curDays,
                // de los registros que hemos actualizado, obtenemos los valores Y de cada variable
                valsYVar1 = getYsEA(registros), // Estado Animo
                valsYVar2 = getYsHS(registros), // Horas Sueño
                valsYVar3 = getYsHA(registros), // Horas Actividades
                eti1 = "Estado animo",
                eti2 = "Horas sueño",
                eti3 = "Actividades",
                colorLineVar1 = resources.getColor(R.color.estado_animo_color_line),
                colorLineVar2 = resources.getColor(R.color.horas_son_color_line),
                colorLineVar3 = resources.getColor(R.color.horas_act_color_line),
                colorFillVar1 = resources.getColor(R.color.estado_animo_color_fill),
                colorFillVar2 = resources.getColor(R.color.horas_son_color_fill),
                colorFillVar3 = resources.getColor(R.color.horas_act_color_fill)
            )
            with(grafico) {

                createXAxis()

                val var1procesados = CreateLineDataSetVar1()
                Log.i(TAG, "Puntos procesados $var1procesados para var1 : Estado Animo")

                val var2procesados = CreateLineDataSetVar2()
                Log.i(TAG, "Puntos procesados $var2procesados para var2 : Horas son")

                val var3procesados = CreateLineDataSetVar3()
                Log.i(TAG, "Puntos procesados $var3procesados para var3 : Horas actividades")

                val ax = mergeDataSets(
                    (binding.cbEstadoAnimo.isChecked && (var1procesados > 0)),
                    (binding.cbHorasSon.isChecked && (var2procesados > 0)),
                    (binding.cbActividades.isChecked && (var3procesados > 0))
                )
                // Si no hemos recuperado puntos de ninguna de las 3 variables, no pedimos el LineData porque será null
//                if(var1procesados > 0 || var2procesados > 0 || var2procesados > 0) lineChart.data = getLineData()
                if (ax > 0) {
                    lineChart.data = getLineData()
                    // Quito la imagen de fondo si ha ido bien
                    binding.ivBackground.visibility = View.GONE
                    // No espera por el click...
                    lineChart.invalidate()
                }

            }


        }
        // Observe the LiveData
        viewModel.registros.observe(viewLifecycleOwner, registrosObserver)
        /**
         * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         */


        /**
         * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         * onClickListeners()
         */
        // Activamos el de 7 días al inicializar la vista, si no, carga la vista el botón clickado, pero no el objeto en memoria
        binding.btnOne.isActivated = true
        // No usado por el momento
        binding.btnOther.isEnabled = false

        // listeners de los botones
        binding.btnOne.setOnClickListener {
            // el isActivated es necesario para el calculo de los días
            curDays = 7
            viewModel.actualizaRegs(curDays)
            binding.btnOne.isActivated = true
            binding.btnTwo.isActivated = false
            binding.btnThree.isActivated = false

        }
        binding.btnTwo.setOnClickListener {
            binding.btnOne.isActivated = false
            binding.btnTwo.isActivated = true
            binding.btnThree.isActivated = false
            curDays = 14
            viewModel.actualizaRegs(curDays)
        }
        binding.btnThree.setOnClickListener {
            binding.btnOne.isActivated = false
            binding.btnTwo.isActivated = false
            binding.btnThree.isActivated = true
            curDays = 30
            viewModel.actualizaRegs(curDays)
        }

        // listeners de los checkboxes
        binding.cbActividades.setOnCheckedChangeListener { _, _ ->
            Log.i(TAG, "cbActividades cambiado.. con $curDays dias.")
            viewModel.actualizaRegs(curDays)
        }

        binding.cbEstadoAnimo.setOnCheckedChangeListener { _, _ ->
            Log.i(TAG, "cbEstadoAnimo cambiado.. con $curDays dias.")
            viewModel.actualizaRegs(curDays)
        }

        binding.cbHorasSon.setOnCheckedChangeListener { _, _ ->
            Log.i(TAG, "cbHorasSon cambiado.. con $curDays dias.")
            viewModel.actualizaRegs(curDays)
        }

        //endregion
        /**
         * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         */
    }

    /**
     * Obtengo los valores Y de cada variable
     */
    private fun getYsHA(regs: ArrayList<Registro>): ArrayList<Float> {
        val ret = ArrayList<Float>()
        for (r in regs) {
            ret.add(r.horasAct.toFloat())
        }
        return ret
    }

    private fun getYsHS(regs: ArrayList<Registro>): ArrayList<Float> {
        val ret = ArrayList<Float>()
        for (r in regs) {
            ret.add(r.horasSon.toFloat())
        }
        return ret
    }

    private fun getYsEA(regs: ArrayList<Registro>): ArrayList<Float> {
        val ret = ArrayList<Float>()
        for (r in regs) {
            ret.add(r.estAnim.toFloat())
        }
        return ret
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            it.supportActionBar?.title = "Vista datos"
            //setHasOptionsMenu(true)
        }
    }


}