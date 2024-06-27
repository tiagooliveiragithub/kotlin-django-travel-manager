package online.tripguru.tripguruapp.views.adapters

import online.tripguru.tripguruapp.localstorage.models.Trip

interface OnTripClickListener {
    fun onTripClick(trip: Trip)
}