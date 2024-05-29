package online.tripguru.tripguruapp.helpers

import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.trip.TripResponse

fun convertResponseToTrip(tripResponse: TripResponse): Trip {
    return Trip(
        id = tripResponse.id,
        name = tripResponse.name,
        description = tripResponse.description
    )
}

fun convertTripToResponse(trip: Trip): TripResponse {
    return TripResponse(
        id = trip.id ?: 0,
        name = trip.name,
        description = trip.description
    )
}