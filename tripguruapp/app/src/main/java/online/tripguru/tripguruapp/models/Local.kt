package online.tripguru.tripguruapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Local(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var tripId: Int? = null,
    var name: String,
    var description: String
)
