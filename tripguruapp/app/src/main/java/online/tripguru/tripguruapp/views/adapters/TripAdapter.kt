package online.tripguru.tripguruapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.databinding.BoxItemBinding
import online.tripguru.tripguruapp.models.Trip


class TripAdapter (
    private val onTripClickListener: OnTripClickListener
): RecyclerView.Adapter<TripAdapter.TripViewHolder>() {
    private var tripList = listOf<Trip>()

    inner class TripViewHolder(private val binding: BoxItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) {
            binding.textViewTitle.text = trip.name
            binding.textViewDescription.text = trip.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemList = BoxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.bind(trip)
        holder.itemView.setOnClickListener {
            onTripClickListener.onTripClick(trip)
        }
    }

    override fun getItemCount(): Int = tripList.size

    fun setTrips(trips: List<Trip>) {
        tripList = trips
        notifyDataSetChanged()
    }
}