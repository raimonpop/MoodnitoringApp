package raimon.example.moodnitoringapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegViewModel: ViewModel() {

    private val TAG = "Log:RegViewModel"

    // Firestore
    private var db = Firebase.firestore

    // LiveData
    //region Estado animo
    val curEstAnim = MutableLiveData<Int>()

    fun updateEA(cur: Int) {
        curEstAnim.postValue(cur)
    }
    //endregion

    //region  Horas sueño
    val curHorasSon = MutableLiveData<Int>()

    fun updateHS(cur: Int) {
        curHorasSon.postValue(cur)
    }
    //endregion

    //region Actividades

    // Fisicas
    val curHorasActFis = MutableLiveData<Int>()

    fun updateHAF(cur: Int) {
        curHorasActFis.postValue(cur)
    }
    // Artisticas
    val curHorasActArt = MutableLiveData<Int>()

    fun updateHAA(cur: Int) {
        curHorasActArt.postValue(cur)
    }
    // Sociales
    val curHorasActSoc = MutableLiveData<Int>()

    fun updateHAS(cur: Int) {
        curHorasActSoc.postValue(cur)
    }
    //endregion

    fun insertReg(reg: Registro){
        Log.i(TAG, "Insertando ${reg}..")

        val auth = FirebaseAuth.getInstance()

        // generate random ID for document
        val id = auth.currentUser?.uid.let {
            db.collection("Registros").document(it.toString())
                .set(reg)
                .addOnSuccessListener {
                    Log.i(TAG, "Registro $reg añadido correctamente")
                }
                .addOnFailureListener { ex ->
                    Log.i(TAG, "Error: ${ex.message}")
                }
            }

        }
}