package raimon.example.moodnitoringapp.ui.actuacion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.ItemCardRecommendationsBinding
import raimon.example.moodnitoringapp.model.Actuacion

class ActuacionAdapter(private val actuacionList: MutableList<Actuacion>, private val listener: iActuacion):RecyclerView.Adapter<ActuacionAdapter.ViewHolder>() {
private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
      val binding = ItemCardRecommendationsBinding.bind(view)

      fun setListener(actuacion: Actuacion){
          binding.root.setOnClickListener {
              listener.openActuacion(actuacion)
          }
      }
    }

    fun add (actuacion: Actuacion){
        if (!actuacionList.contains(actuacion)){
            actuacionList.add(actuacion)
            notifyItemChanged(actuacionList.size - 1 )
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_card_recommendations,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actuacion = actuacionList[position]

        holder.setListener(actuacion)

        holder.binding.tvTitle.text = actuacion.title
        holder.binding.tvDescription.text = actuacion.description
    }

    override fun getItemCount(): Int = actuacionList.size


}
