package by.bashlikovvv.hotelreservation.data.di

import android.content.Context
import android.net.ConnectivityManager
import by.bashlikovvv.hotelreservation.data.remote.HotelApi
import by.bashlikovvv.hotelreservation.data.repository.HotelRepository
import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository
import by.bashlikovvv.hotelreservation.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, factory: Converter.Factory): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Singleton
    fun provideHotelInfoApi(retrofit: Retrofit): HotelApi {
        return retrofit.create(HotelApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHotelRepository(
        @ApplicationContext context: Context,
        hotelApi: HotelApi
    ): IHotelRepository {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return HotelRepository(
            cm = connectivityManager,
            hotelApi = hotelApi
        )
    }
}