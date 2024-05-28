package online.tripguru.tripguruapp.helpers

import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.trip.TripResponse

fun convertResponseToTrip(tripResponse: TripResponse): Trip {
    // Replace with your actual conversion logic
    return Trip(
        id = tripResponse.id,
        tripName = tripResponse.title,
        startDate = tripResponse.description
    )
}