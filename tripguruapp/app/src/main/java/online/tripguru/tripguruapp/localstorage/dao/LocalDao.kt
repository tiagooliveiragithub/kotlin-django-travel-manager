package online.tripguru.tripguruapp.localstorage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import online.tripguru.tripguruapp.localstorage.models.Local

@Dao
interface LocalDao {
     @Query("SELECT * FROM local")
     fun getLocals(): LiveData<List<Local>>
     @Query("SELECT * FROM local WHERE id = :localId")
     fun getLocalById(localId: Int): Local
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(locals: List<Local>)
     @Query("DELETE FROM local")
     fun deleteAll()
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertLocal(local: Local)
     @Query("DELETE FROM local WHERE id = :id")
     fun deleteLocal(id: Int)
     @Query("SELECT * FROM local WHERE tripId = :tripId")
     fun getLocalsForTrip(tripId: Int): LiveData<List<Local>>
     @Query("UPDATE local SET images = :imageUri WHERE id = :localId")
     fun insertImage(localId: Int, imageUri: List<String>)
}