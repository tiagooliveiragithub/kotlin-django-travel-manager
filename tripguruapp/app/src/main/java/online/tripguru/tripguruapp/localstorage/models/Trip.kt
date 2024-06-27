package online.tripguru.tripguruapp.localstorage.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var description: String,
    var startDate: String,
    var endDate: String,
    var image: String? = null,
    var owners: String? = null,
)