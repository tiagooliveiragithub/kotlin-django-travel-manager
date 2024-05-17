package online.tripguru.tripguruapp.views.adapters

import online.tripguru.tripguruapp.models.Trip

interface OnTripClickListener {
    fun onTripClick(trip: Trip)
}