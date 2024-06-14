package online.tripguru.tripguruapp.dependencyinjection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.ConnectivityLiveData
import online.tripguru.tripguruapp.repositories.LocalRepository
import online.tripguru.tripguruapp.repositories.TripRepository
import online.tripguru.tripguruapp.repositories.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideTripRepository(appDatabase: AppDatabase, api: ApiInterface, userRepository: UserRepository): TripRepository {
        return TripRepository(appDatabase, api, userRepository)
    }

    @Singleton
    @Provides
    fun provideLocalRepository(appDatabase: AppDatabase, api: ApiInterface, userRepository: UserRepository): LocalRepository {
        return LocalRepository(appDatabase, api, userRepository)
    }

    @Singleton
    @Provides
    fun provideApi(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://apicm.tiagooliveira.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: ApiInterface, prefs: SharedPreferences, connectivityLiveData: ConnectivityLiveData): UserRepository {
        return UserRepository(api, prefs, connectivityLiveData)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}