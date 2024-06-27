package online.tripguru.tripguruapp.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import online.tripguru.tripguruapp.databinding.BoxItemBinding
import online.tripguru.tripguruapp.localstorage.models.Trip


class TripAdapter(
    private val onTripClickListener: OnTripClickListener,
): RecyclerView.Adapter<TripAdapter.TripViewHolder>() {
    private var tripList = listOf<Trip>()

    inner class TripViewHolder(private val binding: BoxItemBinding, private val context : Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trip: Trip) {
            binding.textViewTitle.text = trip.name
            binding.textViewDate.text = trip.startDate

            Glide.with(itemView.context).load(trip.image).into(binding.tripImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemList = BoxItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(itemList, parent.context)
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