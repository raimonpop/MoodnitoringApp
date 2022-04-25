package raimon.example.moodnitoringapp.model

import java.time.LocalDateTime

data class Registro // https://firebase.google.com/docs/firestore/manage-data/data-types
    (//val id: String // Autogenerado?
    val usuario: String = ""
//    ,var fecha: String = "" // Usamos Datetime format??
    ,val yyyy: Int = LocalDateTime.now().year
    ,var mm: Int = LocalDateTime.now().monthValue
    ,var dd: Int = LocalDateTime.now().dayOfMonth
    ,val estAnim: Int = 0
    ,val horasSon: Int = 0
    ,val horasAct: Int = 0
){ // por dejar la rúbrica completa y si queremos añadir algo...
    override fun toString(): String {
        return "$usuario on $yyyy-$mm-$dd"
    }
}