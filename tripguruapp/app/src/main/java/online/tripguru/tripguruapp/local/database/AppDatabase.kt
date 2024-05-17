package online.tripguru.tripguruapp.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import online.tripguru.tripguruapp.local.dao.LocalDao
import online.tripguru.tripguruapp.local.dao.TripDao
import online.tripguru.tripguruapp.models.Local
import online.tripguru.tripguruapp.models.Trip

@Database(entities = [Trip::class, Local::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun localDao(): LocalDao
}