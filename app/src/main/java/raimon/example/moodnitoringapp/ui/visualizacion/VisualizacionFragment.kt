package raimon.example.moodnitoringapp.UI.visualizacion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.dao.FirestoreDAONC
import raimon.example.moodnitoringapp.databinding.FragmentVisualizacionBinding
import raimon.example.moodnitoringapp.model.Registro
import raimon.example.moodnitoringapp.ui.visualizacion.ChartAdapter
import java.time.LocalDate


class VisualizacionFragment : Fragment(), ChartAdapter {

//    private lateinit var binding: FragmentVisualizacionBinding
    private val TAG = "Log:VisFrag"

    // ViewBinding
    private var _binding: FragmentVisualizacionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // LineCharts
    private lateinit var lineChart: LineChart
    private lateinit var registrosFirebase: ArrayList<Registro>

    // ArrayList con todos los LineDataSets que vamos a dibujar
    private lateinit var dataSets: ArrayList<ILineDataSet>

    // Cada una de las variables..
    // Entry
    private lateinit var lineEntryEstAnim: ArrayList<Entry>
    private lateinit var lineEntryHorasSon: ArrayList<Entry>
    private lateinit var lineEntryHorasAct: ArrayList<Entry>
    //LineDataSets
    private lateinit var lineDataSetEstAnim: LineDataSet
    private lateinit var lineDataSetHorasSon: LineDataSet
    private lateinit var lineDataSetHorasAct: LineDataSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_visualizacion, container, false)
        //return inflater.inflate(R.layout.fragment_home, container, false)
        _binding = FragmentVisualizacionBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos el ArrayList
        registrosFirebase = ArrayList()

        // Activamos el de 7 días al inicializar la vista, si no, carga la vista el botón clickado, pero no el objeto en memoria
        binding.btnOne.isActivated = true
        // No usado por el momento
        binding.btnOther.isEnabled = false

        // Inicializamos el LineChart con la vista por defecto (todas las variables y los últimos 7 días)
        lineChart = binding.lcMainChart
        setLineChartData()

        // listeners de los botones
        binding.btnOne.setOnClickListener {
            // el isActivated es necesario para el calculo de los días
            binding.btnOne.isActivated = true
            binding.btnTwo.isActivated = false
            binding.btnThree.isActivated = false
            setLineChartData()
        }
        binding.btnTwo.setOnClickListener {
            binding.btnOne.isActivated = false
            binding.btnTwo.isActivated = true
            binding.btnThree.isActivated = false
            setLineChartData()
        }
        binding.btnThree.setOnClickListener {
            binding.btnOne.isActivated = false
            binding.btnTwo.isActivated = false
            binding.btnThree.isActivated = true
            setLineChartData()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Convierte los objetos de Firestore en ArrayLists de Float para las gráficas
     *
     * ChartAdapter
     */

    override fun getHorasSon(regs: ArrayList<Registro>): ArrayList<Float> {
        val ret  = ArrayList<Float>()
        for(r in regs){
            ret.add(r.horasSon.toFloat())
        }
        return ret
    }

    override fun getEstAnim(regs: ArrayList<Registro>): ArrayList<Float> {
        val ret  = ArrayList<Float>()
        for(r in regs){
            ret.add(r.estAnim.toFloat())
        }
        return ret
    }

    override fun getHorasAct(regs: ArrayList<Registro>): ArrayList<Float> {
        val ret  = ArrayList<Float>()
        for(r in regs){
            ret.add(r.horasAct.toFloat())
        }
        return ret
    }

    /**
     * Set data in chart
     */
    private fun setLineChartData() {
        // Variable que gestiona si dibujamos o no el LineChart
        var abortPrint = false
        // Fechas para "VERSION.SDK_INT < 26"
        var dias: Long = 0
        // Rango de tiempo
        when{
            binding.btnOne.isActivated -> dias = 7
            binding.btnTwo.isActivated -> dias = 14
            binding.btnThree.isActivated -> dias = 30
            else -> { Toast.makeText(activity,"No hay rango de tiempo seleccionado", Toast.LENGTH_SHORT).show()}
        }
        val dateStart =  LocalDate.now().minusDays(dias)
        Log.i(TAG, "Seleccionado $dias dias -> dateStart=$dateStart")
        // Si hay un periodo de días diferente de 0
        if (dias != 0L){
            // 1.1 Definimos ArrayList
            val xValues = ArrayList<String>()
            // 1.2 Lo llenamos con las fechas en formato String, desde la fecha de inicio, los dias que nos pidan
            for (i in 0..dias-1){
                xValues.add(dateStart.plusDays(i).toString())
            }
                // Recibimos de Firebase (FirestoneDAONC) un ArrayList<Registros>
            val temp = FirestoreDAONC.getRegisters(dias.toInt())
            registrosFirebase.addAll(temp)

                    // De los registros evaluamos 2 cosas:
                    // 2.1.1. Hay registros
                    if (registrosFirebase.size > 0){
                        // Inicializamos la variable de los dataSets
                        dataSets = ArrayList()
                        // 2.1.2. Cual es el LineChart a pintar
                        when{
                            binding.cbEstadoAnimo.isChecked -> setEstAniLineDataSet( resources.getColor(R.color.estado_animo_color_line), resources.getColor(R.color.estado_animo_color_fill))
                            binding.cbHorasSon.isChecked -> setHorasSonLineDataSet(resources.getColor(R.color.horas_son_color_line), resources.getColor(R.color.horas_son_color_fill))
                            binding.cbActividades.isChecked -> setActividadesLineDataSet(resources.getColor(R.color.horas_act_color_line), resources.getColor(R.color.horas_act_color_fill))
                            else -> {
                                Toast.makeText(activity,"No hay ninguna variable seleccionada", Toast.LENGTH_SHORT).show()
                                abortPrint = true
                            }
                        }

                        // Si no hemos abortado pintar la gráfica...
                        if(!abortPrint){
                            // 4.1 Creamos los datos
                            // val data = LineData(lineDataSet) --> Para un solo LineDataSet
                            val data = LineData(dataSets) // Para varios, le meto el array
                            // 5 Setup del LineChart
                            lineChart.data = data
                            lineChart.setBackgroundColor(resources.getColor(R.color.white))
                            lineChart.animateXY(3000,3000)
                        }
                    }else{
                        // Evitamos el IndexOutOfBoundsException y avisamos al usuario
                        Toast.makeText(activity,"No tienes registros en este rango de tiempo seleccionado", Toast.LENGTH_LONG).show()
                    }
        }
    }

    /**
     * ESTADO DE ANIMO LineEntry, LineDataSet, y se añade el LineDataSet al dataSets
     */
    private fun setEstAniLineDataSet(color: Int, colorFill: Int) {
        // 2.1 Creamos los valores del eje Y para Horas de sueño
        lineEntryEstAnim = ArrayList() // Cuando vuelvo a ejecutar el método, lo vuelve a inicializar?

        // Recuperamos la lista de Float de cada una de las variables, en este caso EstAni
        val yValues :ArrayList<Float> = getEstAnim(registrosFirebase)
        // Lo convertimos en entries. (Entry::com.github.mikephil.charting.data.Entry)
        var pointer = 0
        for (j in yValues){
            lineEntryEstAnim.add(Entry( pointer.toFloat(),j))
            pointer++
        }
        // 3.1 Creamos el lineDataSet
        // Añadimos los Entry
        lineDataSetEstAnim = LineDataSet(lineEntryEstAnim,"Estado de ánimo")
        // Cambiamos el formato
        lineDataSetEstAnim.color = color // resources.getColor(R.color.purple_500) // Color de la linea
        lineDataSetEstAnim.circleRadius = 0f // Quita los círculos
        lineDataSetEstAnim.setDrawFilled(true) // Rellena
        lineDataSetEstAnim.fillColor = colorFill//resources.getColor(R.color.purple_200) // Color de relleno
        lineDataSetEstAnim.fillAlpha = 30

        // 4.0 Agrupamos los LineDataSets en un array
        dataSets.add(lineDataSetEstAnim)
    }

    /**
     * HORAS DE SUEÑO LineEntry, LineDataSet, y se añade el LineDataSet al dataSets
     */
    private fun setHorasSonLineDataSet(color: Int, colorFill: Int) {
        // 2.1 Creamos los valores del eje Y para Horas de sueño
        lineEntryHorasSon = ArrayList() // Cuando vuelvo a ejecutar el método, lo vuelve a inicializar?
        // Recuperamos la lista de Float de cada una de las variables, en este caso EstAni
        val yValues :ArrayList<Float> = getHorasSon(registrosFirebase)
        // Lo convertimos en entries. (Entry::com.github.mikephil.charting.data.Entry)
        var pointer = 0
        for (j in yValues){
            lineEntryHorasSon.add(Entry( pointer.toFloat(),j))
            pointer++
        }
        // 3.1 Creamos el lineDataSet
        // Añadimos los Entry
        lineDataSetHorasSon = LineDataSet(lineEntryHorasSon,"Estado de ánimo")
        // Cambiamos el formato
        lineDataSetHorasSon.color = color // resources.getColor(R.color.purple_500) // Color de la linea
        lineDataSetHorasSon.circleRadius = 0f // Quita los círculos
        lineDataSetHorasSon.setDrawFilled(true) // Rellena
        lineDataSetHorasSon.fillColor = colorFill//resources.getColor(R.color.purple_200) // Color de relleno
        lineDataSetHorasSon.fillAlpha = 30

        // 4.0 Agrupamos los LineDataSets en un array
        dataSets.add(lineDataSetHorasSon)
    }

    /**
     * HORAS DE ACTIVIDADES LineEntry, LineDataSet, y se añade el LineDataSet al dataSets
     */
    private fun setActividadesLineDataSet(color: Int, colorFill: Int) {
        // 2.1 Creamos los valores del eje Y para Horas de sueño
        lineEntryHorasAct = ArrayList() // Cuando vuelvo a ejecutar el método, lo vuelve a inicializar?
        // Recuperamos la lista de Float de cada una de las variables, en este caso EstAni
        val yValues :ArrayList<Float> = getHorasAct(registrosFirebase)
        // Lo convertimos en entries. (Entry::com.github.mikephil.charting.data.Entry)
        var pointer = 0
        for (j in yValues){
            lineEntryHorasAct.add(Entry( pointer.toFloat(),j))
            pointer++
        }
        // 3.1 Creamos el lineDataSet
        // Añadimos los Entry
        lineDataSetHorasAct = LineDataSet(lineEntryHorasAct,"Estado de ánimo")
        // Cambiamos el formato
        lineDataSetHorasAct.color = color // resources.getColor(R.color.purple_500) // Color de la linea
        lineDataSetHorasAct.circleRadius = 0f // Quita los círculos
        lineDataSetHorasAct.setDrawFilled(true) // Rellena
        lineDataSetHorasAct.fillColor = colorFill//resources.getColor(R.color.purple_200) // Color de relleno
        lineDataSetHorasAct.fillAlpha = 30

        // 4.0 Agrupamos los LineDataSets en un array
        dataSets.add(lineDataSetHorasAct)
    }

}