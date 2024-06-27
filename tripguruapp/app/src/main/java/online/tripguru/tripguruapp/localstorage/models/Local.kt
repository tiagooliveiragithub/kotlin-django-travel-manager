package online.tripguru.tripguruapp.localstorage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Local(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var tripId: Int? = null,
    var name: String,
    var description: String,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var address: String? = null,
    var images: List<String>? = null
)
