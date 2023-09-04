package by.bashlikovvv.hotelreservation.data.di

import android.content.Context
import android.net.ConnectivityManager
import by.bashlikovvv.hotelreservation.data.remote.HotelApi
import by.bashlikovvv.hotelreservation.data.remote.ReservationApi
import by.bashlikovvv.hotelreservation.data.remote.RoomsApi
import by.bashlikovvv.hotelreservation.data.repository.HotelRepository
import by.bashlikovvv.hotelreservation.data.repository.ReservationRepository
import by.bashlikovvv.hotelreservation.data.repository.RoomsRepository
import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository
import by.bashlikovvv.hotelreservation.domain.repository.IReservationRepository
import by.bashlikovvv.hotelreservation.domain.repository.IRoomsRepository
import by.bashlikovvv.hotelreservation.utils.Constants
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder().build()
    }

    single<Converter.Factory> {
        GsonConverterFactory.create()
    }

    single<Retrofit> {
        val okHttpClient: OkHttpClient = get()
        val factory: Converter.Factory = get()

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(factory)
            .build()
    }

    single<HotelApi> {
        val retrofit: Retrofit = get()

        retrofit.create(HotelApi::class.java)
    }

    single<RoomsApi> {
        val retrofit: Retrofit = get()

        retrofit.create(RoomsApi::class.java)
    }

    single<ReservationApi> {
        val retrofit: Retrofit = get()

        retrofit.create(ReservationApi::class.java)
    }

    single<IHotelRepository> {
        val context: Context = get()
        val hotelApi: HotelApi = get()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        HotelRepository(
            cm = connectivityManager,
            hotelApi = hotelApi
        )
    }

    single<IRoomsRepository> {
        val context: Context = get()
        val roomsApi: RoomsApi = get()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        RoomsRepository(
            cm = connectivityManager,
            roomsApi = roomsApi
        )
    }

    single<IReservationRepository> {
        val reservationApi: ReservationApi = get()

        ReservationRepository(reservationApi)
    }
}