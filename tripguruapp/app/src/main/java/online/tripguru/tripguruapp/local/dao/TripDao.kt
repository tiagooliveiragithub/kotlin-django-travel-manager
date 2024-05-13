package online.tripguru.tripguruapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import online.tripguru.tripguruapp.models.Trip

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getTrips(): LiveData<List<Trip>>

    @Insert
    fun insert(trip: Trip)
}
