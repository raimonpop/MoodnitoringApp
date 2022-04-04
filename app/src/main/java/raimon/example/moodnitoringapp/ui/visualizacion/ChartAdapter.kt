package raimon.example.moodnitoringapp.ui.visualizacion

import raimon.example.moodnitoringapp.model.Registro

interface ChartAdapter {
    fun getHorasSon(regs: ArrayList<Registro>): ArrayList<Float>
    fun getEstAnim(regs: ArrayList<Registro>): ArrayList<Float>
    fun getHorasAct(regs: ArrayList<Registro>): ArrayList<Float>

}