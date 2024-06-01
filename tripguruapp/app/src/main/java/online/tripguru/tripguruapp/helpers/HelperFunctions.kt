package online.tripguru.tripguruapp.helpers

import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip
import online.tripguru.tripguruapp.network.LocalResponse
import online.tripguru.tripguruapp.network.TripResponse

fun convertResponseToTrip(tripResponse: TripResponse): Trip {
    return Trip(
        id = tripResponse.id,
        name = tripResponse.name,
        description = tripResponse.description
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