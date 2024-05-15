package online.tripguru.tripguruapp.dependencyinjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.repositories.TripRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "trip_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTripRepository(appDatabase: AppDatabase): TripRepository {
        return TripRepository(appDatabase)
    }
}