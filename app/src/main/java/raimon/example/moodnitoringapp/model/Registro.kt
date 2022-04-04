package raimon.example.moodnitoringapp.model

data class Registro // https://firebase.google.com/docs/firestore/manage-data/data-types
    (//val id: String // Autogenerado?
    val usuario: String = ""
    ,val fecha: String = "" // Usamos Datetime format??
    ,val estAnim: Int = 0
    ,val horasSon: Int = 0
    ,val horasAct: Int = 0
){ // por dejar la rúbrica completa y si queremos añadir algo...
//    fun getIdName(): String = "$id $usuario"
}


