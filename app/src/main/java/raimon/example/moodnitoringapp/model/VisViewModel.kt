package raimon.example.moodnitoringapp.model

import android.text.format.Time
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.Timestamp.CREATOR
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min
import kotlin.time.Duration.Companion.days

class VisViewModel : ViewModel() {

    private val TAG = "Log:VisViewModel"

    // Pendiente de revisar
    private lateinit var regs: ArrayList<Registro>

    private val auth = FirebaseAuth.getInstance()


    // LiveData
    val registros = MutableLiveData<ArrayList<Registro>>()

    fun actualizaRegs(dias: Int) {
        regs = ArrayList<Registro>()
//        doAsync {
        getRegisters(dias)
//            uiThread {
//                Log.i(TAG, "Posteo los registros para el observer")
        registros.postValue(regs)
//            }
//        }
    }

    /**
     * Fetch registros de Firestore y los actualiza en el obj regs
     */
    fun getRegisters(dias: Int) {
        Log.d(TAG, "Numero de dias $dias")
        // Calculo de fecha ini + final
        val fechaFin = Timestamp.now().toDate()
        //val fechaIni = fechaFin.time. //dias.toLong()// "2022.01.01"
        //val control1 = fechaFin.time.days
        //val control2 = fechaIni
        //Log.i(TAG, "getRegisters desde $fechaIni hasta $fechaFin")

        // Reference collection
        val ref = FirebaseFirestore.getInstance().collection("Registros")
        // Query
        ref
//            .whereEqualTo("usuario","pepe@test.com")
            .whereEqualTo("usuario", auth.currentUser?.uid.toString())
            /*.whereGreaterThanOrEqualTo("fecha", Timestamp.now()/*fechaIni*/)
            .whereLessThanOrEqualTo("fecha", Timestamp.now())*/
            .orderBy("fecha", Query.Direction.DESCENDING).limit(dias.toLong())
            .get().addOnSuccessListener { docs ->
                Log.d(TAG, "Recuperados: ${docs.size()} documentos. en vista")
                var listRegs = docs.toObjects(Registro::class.java)
                listRegs.forEach { docs -> regs.add(docs) }
                Log.d(TAG, "Devolviendo ${regs.size} de Firebase en vista")
                registros.postValue(regs)
            }.addOnFailureListener {
                Log.i(TAG, "Error: ${it.message}")
            }

    }

}
