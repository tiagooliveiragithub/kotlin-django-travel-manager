package online.tripguru.tripguruapp.localstorage.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.tripguru.tripguruapp.localstorage.dao.LocalDao
import online.tripguru.tripguruapp.localstorage.dao.TripDao
import online.tripguru.tripguruapp.localstorage.models.Local
import online.tripguru.tripguruapp.localstorage.models.Trip

@Database(entities = [Trip::class, Local::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun localDao(): LocalDao
}