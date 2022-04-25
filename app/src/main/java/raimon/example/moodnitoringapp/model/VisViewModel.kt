package raimon.example.moodnitoringapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class VisViewModel: ViewModel() {

    private val TAG = "Log:VisViewModel"
    // Pendiente de revisar
    private lateinit var regs : ArrayList<Registro>

    // LiveData
    val registros = MutableLiveData<ArrayList<Registro>>()

    fun actualizaRegs(dias:Int, correo:String){
        regs = ArrayList<Registro>()
//        doAsync {
            getRegisters(dias, correo)
//            uiThread {
//                Log.i(TAG, "Posteo los registros para el observer")
////                registros.postValue(regs)
//            }
//        }
    }

    /**
     * Fetch registros de Firestore y los actualiza en el obj regs
     */
    fun getRegisters(dias:Int, correo:String) {

        // Calculo de fecha ini + final
        val fechaFin= LocalDate.now().toString()
        val fechaIni =  LocalDate.now().minusDays(dias.toLong()).toString() // "2022.01.01"
        Log.i(TAG, "getRegisters desde $fechaIni hasta $fechaFin")

        // Reference collection
        val ref = Firebase.firestore.collection("Registros")
        // Query
        ref
//            .whereEqualTo("usuario","pepe@test.com")
            .whereEqualTo("usuario", correo)
            .whereGreaterThanOrEqualTo("fecha", fechaIni)
            .whereLessThanOrEqualTo("fecha", fechaFin)
            .get().addOnSuccessListener { docs ->
                Log.d(TAG, "Recuperados: ${docs.size()} documentos.")
                val listRegs = docs.toObjects(Registro::class.java)
                for(d in listRegs){
                    Log.d(TAG, d.toString())
                    regs.add(d)
                }
                Log.d(TAG, "Devolviendo ${regs.size} de Firebase")
                registros.postValue(regs)
            }.addOnFailureListener {
                Log.i(TAG, "Error: ${it.message}")
            }

    }

}
