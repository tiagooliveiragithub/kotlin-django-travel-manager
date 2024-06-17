package online.tripguru.tripguruapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.databinding.BoxItemVerticalBinding
import online.tripguru.tripguruapp.models.Local

class LocalAdapter (
    private val onLocalClickListener: OnLocalClickListener
) : RecyclerView.Adapter<LocalAdapter.LocalViewHolder>() {

    private var localList = listOf<Local>()

    inner class LocalViewHolder(private val binding: BoxItemVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(local: Local) {
            binding.textViewName.text = local.name
            binding.textViewDescription.text = local.description
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

    fun setLocals(locals: List<Local>) {
        this.localList = locals
        notifyDataSetChanged()
    }
}