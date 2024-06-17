package online.tripguru.tripguruapp.dependencyinjection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.tripguru.tripguruapp.local.database.AppDatabase
import online.tripguru.tripguruapp.network.ApiInterface
import online.tripguru.tripguruapp.network.ConnectivityLiveData
import online.tripguru.tripguruapp.repositories.LocalRepository
import online.tripguru.tripguruapp.repositories.LocationRepository
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
            .baseUrl("http://10.0.2.2:8000/")
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

    @Provides
    @Singleton
    fun provideGlideInstance(application: Application): RequestManager {
        return Glide.with(application)
    }

    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    fun provideLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ): LocationRepository {
        return LocationRepository(fusedLocationProviderClient, context)
    }

}