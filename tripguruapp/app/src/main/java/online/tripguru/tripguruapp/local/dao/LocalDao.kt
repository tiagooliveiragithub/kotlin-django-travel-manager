package online.tripguru.tripguruapp.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import online.tripguru.tripguruapp.models.Local

@Dao
interface LocalDao {
     @Query("SELECT * FROM local")
     fun getLocals(): LiveData<List<Local>>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(locals: List<Local>)

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertLocals(locals: List<Local>)
}