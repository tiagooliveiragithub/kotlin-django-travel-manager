package online.tripguru.tripguruapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.R


class TripAdapter(entityList: List<Trip>) :
    RecyclerView.Adapter<TripAdapter.TripViewHolder>() {
    private val entityList: List<Trip>

    init {
        this.entityList = entityList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.box_item, parent, false)
        return TripViewHolder(view)
    }


    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip: Trip = entityList[position]

        // Bind trip data to views in ViewHolder
        holder.textViewTitle.text = trip.tripName
        holder.textViewDate.text = trip.startDate

        // Set click listener if needed
        holder.itemView.setOnClickListener {
            // Handle item click event here
            // You can use position to get the clicked item
        }
    }

    override fun getItemCount(): Int {
        return entityList.size
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}