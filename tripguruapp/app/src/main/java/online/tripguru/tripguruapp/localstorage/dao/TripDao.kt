package online.tripguru.tripguruapp.localstorage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import online.tripguru.tripguruapp.models.Trip

@Dao
interface TripDao {
    @Query("SELECT * FROM trip")
    fun getTrips(): LiveData<List<Trip>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(trips: List<Trip>)
    @Query("DELETE FROM trip")
    fun deleteAll()
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrip(trip: Trip)
    @Query("DELETE FROM trip WHERE id = :id")
    fun deleteTrip(id: Int)
}
