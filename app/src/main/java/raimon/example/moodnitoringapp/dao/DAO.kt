package raimon.example.moodnitoringapp.dao

import raimon.example.moodnitoringapp.model.Registro
import java.util.ArrayList

// https://firebase.google.com/docs/firestore/query-data/get-data#kotlin+ktx
interface DAO {

    // Añadir registro diario
    fun addRegister(reg: Registro): Int

    // Query
    fun getRegisters(dias: Int): ArrayList<Registro>

    fun deleteRegister(reg: Registro)

    fun updateRegister(reg: Registro)

}