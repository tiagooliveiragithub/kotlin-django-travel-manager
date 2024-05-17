package online.tripguru.tripguruapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.databinding.BoxItemVerticalBinding
import online.tripguru.tripguruapp.models.Local

class LocalAdapterVertical (
    private val onLocalClickListener: OnLocalClickListener
) : RecyclerView.Adapter<LocalAdapterVertical.LocalViewHolder>() {

    private var localList = listOf<Local>()

    inner class LocalViewHolder(private val binding: BoxItemVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(local: Local) {
            binding.textViewTitle.text = local.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalViewHolder {
        val itemList = BoxItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocalViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: LocalViewHolder, position: Int) {
        val local = localList[position]
        holder.bind(local)
        holder.itemView.setOnClickListener {
            onLocalClickListener.onLocalClick(local)
        }
    }

    override fun getItemCount(): Int = localList.size

    // Método para atualizar a lista de viagens
    fun setLocals(locals: List<Local>) {
        localList = locals
        notifyDataSetChanged()
    }
}