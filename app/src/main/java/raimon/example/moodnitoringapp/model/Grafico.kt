package raimon.example.moodnitoringapp.model

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.time.LocalDateTime
import java.util.ArrayList

data class Grafico(
    // dias
    var dias: Int = 0
//    , private val dateEnd: String //= Timestamp.now().toDate().toString()

    // 1. EJE X
//        , private var ejeX: ArrayList<String>
    // 2. EJE Y
    // Valores de Y para cada una de las variables
    ,
    var valsYVar1: ArrayList<Float> = ArrayList(),
    var valsYVar2: ArrayList<Float> = ArrayList(),
    var valsYVar3: ArrayList<Float> = ArrayList()
    // Etiquetas
    ,
    var eti1: String = "",
    var eti2: String = "",
    var eti3: String = ""
//   // Entry(ies)
//    , private var ejeY_ea: ArrayList<Entry>
//    , private var ejeY_hs: ArrayList<Entry>
//    , private var ejeY_act: ArrayList<Entry>
//   // 3. LineDataSets
//    , private var lineDataSet_ea: LineDataSet
//    , private var lineDataSet_hs: LineDataSet
//    , private var lineDataSet_act: LineDataSet
    // Colores
    ,
    var colorLineVar1: Int,
    var colorFillVar1: Int,
    var colorLineVar2: Int,
    var colorFillVar2: Int,
    var colorLineVar3: Int,
    var colorFillVar3: Int
    // 4. DataSets
    ,
    private var dataSets: ArrayList<ILineDataSet> = ArrayList()
) {
    private lateinit var ejeX: ArrayList<String>
    private lateinit var ejeY_ea: ArrayList<Entry>
    private lateinit var ejeY_hs: ArrayList<Entry>
    private lateinit var ejeY_act: ArrayList<Entry>
    private lateinit var lineDataSet_ea: LineDataSet
    private lateinit var lineDataSet_hs: LineDataSet
    private lateinit var lineDataSet_act: LineDataSet

    // 5. LineData
    private lateinit var data: LineData

    // Crea el eje X
    fun createXAxis(): Int {
        val dateStart = LocalDateTime.now().minusDays(dias.toLong())

        if (dias > 0) {
            ejeX = ArrayList()
            // 1.2 Lo llenamos con las fechas en formato String, desde la fecha de inicio, los dias que nos pidan
            for (i in 0..dias - 1) {
                ejeX.add(dateStart.plusDays(i.toLong()).toString())
            }
        }
        return dias
    }

    /**
     * Devuelve el numero de puntos (x,y) procesados
     */
    // Crea el Eje Y + LineDataSet
    fun CreateLineDataSetVar1(): Int {
        var ret: Int
        if (valsYVar1.size < 1) {
            ret = -1
        } else {
            ejeY_hs = ArrayList()

            var pointer = 0
            for (j in valsYVar2) {
                // Los añado al array de Entry
                ejeY_hs.add(Entry(pointer.toFloat(), j))
                pointer++
            }
            lineDataSet_hs = LineDataSet(ejeY_hs, eti2)
            lineDataSet_hs.color =
                colorLineVar2 // resources.getColor(R.color.purple_500) // Color de la linea
            lineDataSet_hs.circleRadius = 1f // Quita los círculos
            lineDataSet_hs.setDrawFilled(true) // Rellena
            lineDataSet_hs.fillColor =
                colorFillVar2//resources.getColor(R.color.purple_200) // Color de relleno
            lineDataSet_hs.fillAlpha = 30
            ret = pointer
        }
        return ret
    }

    /**
     * Devuelve el numero de puntos (x,y) procesados
     */
    // Crea el Eje Y + LineDataSet
    fun CreateLineDataSetVar2(): Int {
        var ret: Int
        if (valsYVar2.size < 1) {
            ret = -1
        } else {
            ejeY_act = ArrayList()

            var pointer = 0
            for (j in valsYVar3) {
                // Los añado al array de Entry
                ejeY_act.add(Entry(pointer.toFloat(), j))
                pointer++
            }
            lineDataSet_act = LineDataSet(ejeY_act, eti3)
            lineDataSet_act.color =
                colorLineVar3 // resources.getColor(R.color.purple_500) // Color de la linea
            lineDataSet_act.circleRadius = 1f // Quita los círculos
            lineDataSet_act.setDrawFilled(true) // Rellena
            lineDataSet_act.fillColor =
                colorFillVar3//resources.getColor(R.color.purple_200) // Color de relleno
            lineDataSet_act.fillAlpha = 30
            ret = pointer
        }
        return ret
    }

    /**
     * Devuelve el numero de puntos (x,y) procesados
     */
    // Crea el Eje Y + LineDataSet
    fun CreateLineDataSetVar3(): Int {
        var ret: Int
        if (valsYVar3.size < 1) {
            ret = -1
        } else {
            ejeY_ea = ArrayList()
            var pointer = 0
            for (j in valsYVar1) {
                // Los añado al array de Entry
                ejeY_ea.add(Entry(pointer.toFloat(), j))
                pointer++
            }
            lineDataSet_ea = LineDataSet(ejeY_ea, eti1)
            lineDataSet_ea.color =
                colorLineVar1 // resources.getColor(R.color.purple_500) // Color de la linea
            lineDataSet_ea.circleRadius = 1f // Quita los círculos
            lineDataSet_ea.setDrawFilled(true) // Rellena
            lineDataSet_ea.fillColor =
                colorFillVar1//resources.getColor(R.color.purple_200) // Color de relleno
            lineDataSet_ea.fillAlpha = 30
            ret = pointer
        }
        return ret
    }

    /**
     * Devuelve cuantos ha actualizado ó -1 con error
     */
    fun mergeDataSets(var1: Boolean, var2: Boolean, var3: Boolean): Int {
        var ret: Int = 0
        if (!var1 && !var2 && !var3) {
            // No hay variable seleccionada ¿¿??
            ret = -1
        } else {
            if (var1) {
                dataSets.add(lineDataSet_ea)
                ret++
            }
            if (var2) {
                dataSets.add(lineDataSet_hs)
                ret++
            }
            if (var3) {
                dataSets.add(lineDataSet_act)
                ret++
            }
            // Crea el LineData con los dataSets
            data = LineData(dataSets)
        }
        return ret
    }

    fun getLineData(): LineData {
        return data
    }


    // Ejemplo de uso:
//    Grafico(...)
//    createXAxis()
//    CreateLineDataSetVar1()
//    CreateLineDataSetVar2()
//    CreateLineDataSetVar3()
//    mergeDataSets()
//    getLineData()
}