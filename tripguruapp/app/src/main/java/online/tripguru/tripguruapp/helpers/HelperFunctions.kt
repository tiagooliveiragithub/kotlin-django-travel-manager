package online.tripguru.tripguruapp.helpers

import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.trip.LocalResponse
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

fun convertResponseToLocal(localResponse: LocalResponse): Local {
    return Local(
        id = localResponse.id,
        tripId = localResponse.tripId,
        name = localResponse.name,
        description = localResponse.description
    )
}

fun convertLocalToResponse(local: Local): LocalResponse {
    return LocalResponse(
        id = local.id ?: 0,
        tripId = local.tripId ?: 0,
        name = local.name,
        description = local.description
    )
}