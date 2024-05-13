package online.tripguru.tripguruapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var tripName: String,
    var startDate: String
)