package raimon.example.moodnitoringapp.dao

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import raimon.example.moodnitoringapp.model.Registro
import java.time.LocalDate
import java.util.ArrayList

/**
 * NC -> No Coroutines
 *
 *      * Database schema:
 *      +Usuarios
 *          -carlos@gmail.com // random ID
 *              -Registros
 *                  - DcHx1BT0bLhuULd1djr2 (random ID)
 *                      fecha: "2022-03-31"
 *                      horasSon: 9
 *                      estAnim: 9
 *                      horasAct: 16
 */

// Object es el equivalente del patrón Singleton en Kotlin, se usa el init{} para cuando lo instancias
object FirestoreDAONC: DAO {

    private const val TAG = "Log:FirestoreDAONC"
    private var db: FirebaseFirestore
    private var auth: FirebaseAuth
    private var uid: String
    private lateinit var mail: String

    // Cuando instancias el Singleton
    init{
        Log.i(TAG,"Singleton:FirestoreDAONC invocated.")
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        uid = auth.uid.toString()
        if(auth.currentUser != null) mail = auth.currentUser!!.email.toString() else mail = "carlos@test.com" // Eliminar cuando se haya implementado Auth.
//        mail = "${auth.currentUser!!.email}"
        Log.i(TAG,"Singleton: - UID: $uid con correo: $mail")
    }

    /***
     * DAO OVERRIDES
     */
    override fun addRegister(reg: Registro): Int {
        // Devuelvo -1 a no ser que sea correcto y devuelvo 0
        var ret: Int = -1
        // temp val para INSERT
        val registro = mapOf(
            "usuario" to reg.usuario // probar con "usuario" to mail
            ,"fecha" to reg.fecha
            ,"horasSon" to reg.horasSon
            ,"estAnim" to reg.estAnim
            ,"horasAct" to reg.horasAct
        )
        Log.i(TAG, "INSERT Registro de userID: $uid ($mail) $reg ") // Revisar en runtime si el $reg imprime bien el toString()
        db.collection("Registros")
            .add(registro)
            .addOnSuccessListener {
                Log.i(TAG,"Registro actualizado correctamente")
                ret = 0
            }
            .addOnFailureListener {
                Log.i(TAG,"Error insertando el registro")
            }
        return ret
    }

    override fun getRegisters(dias: Int): ArrayList<Registro> {
        Log.d(TAG, "getRegisters() on duty...")
        // Declaramos el return
        val ret = ArrayList<Registro>()
        // Calculo de fecha ini + final

        val fechaFin= LocalDate.now().toString()
        val fechaIni =  LocalDate.now().minusDays(dias.toLong()).toString() // "2022.01.01"
        Log.i(TAG, "getRegisters desde $fechaIni hasta $fechaFin")

        val ref = db.collection("Registros")

        Log.d(TAG, "$mail")
        // Query
        ref
            .whereEqualTo("usuario",mail)
            .whereGreaterThanOrEqualTo("fecha", fechaIni)
            //.whereLessThanOrEqualTo("fecha", fechaFin)
            .get().addOnSuccessListener { docs ->
                //        query.get().addOnSuccessListener { docs ->
                Log.d(TAG, "Recuperados: ${docs.size()} documentos.")
                val listRegs = docs.toObjects(Registro::class.java)
                for(d in listRegs){
                    Log.d(TAG, d.toString())
                    ret.add(d)
                }
            }.addOnFailureListener {
                Log.i(TAG, "Error: ${it.message}")
            }
            Log.d(TAG, "Devolviendo ${ret.size} de Firebase")
            return ret
    }

    override fun deleteRegister(reg: Registro) {
        Log.i(TAG,"Not will be implemented")
    }

    override fun updateRegister(reg: Registro) {
        Log.i(TAG,"Not will be implemented")
    }


}