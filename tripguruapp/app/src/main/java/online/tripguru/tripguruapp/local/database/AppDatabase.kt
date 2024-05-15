package online.tripguru.tripguruapp.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import online.tripguru.tripguruapp.local.dao.TripDao
import online.tripguru.tripguruapp.models.Trip

@Database(entities = [Trip::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}