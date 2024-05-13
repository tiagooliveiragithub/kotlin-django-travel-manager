package online.tripguru.tripguruapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import online.tripguru.tripguruapp.R
import online.tripguru.tripguruapp.models.Trip


class TripAdapter(entityList: List<Trip>) :
    RecyclerView.Adapter<TripAdapter.TripViewHolder>() {
    private var entityList: List<Trip>

    init {
        this.entityList = entityList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.box_item, parent, false)
        return TripViewHolder(view)
    }


    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip: Trip = entityList[position]

        holder.textViewTitle.text = trip.tripName
        holder.textViewDate.text = trip.startDate

        holder.itemView.setOnClickListener {
            // Handle item click
        }
    }

    override fun getItemCount(): Int {
        return entityList.size
    }

    fun setTrips(it: List<Trip>) {
        entityList = it
        notifyDataSetChanged()
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }
}