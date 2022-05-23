package raimon.example.moodnitoringapp.ui.recaida

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.ItemNoteBinding
import raimon.example.moodnitoringapp.model.Notes

class RecaidaAdapter(var noteList: MutableList<Notes>): RecyclerView.Adapter<RecaidaAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemNoteBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val note = noteList.get(position)
        holder.binding.tvDesciption.text = note.text
    }

    override fun getItemCount(): Int = noteList.size

    fun add(note: Notes){
        noteList.add(note)
        notifyItemChanged(noteList.size - 1)
    }
}