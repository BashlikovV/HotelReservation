package by.bashlikovvv.hotelreservation.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import by.bashlikovvv.data.local.HotelReservationDatabase
import by.bashlikovvv.data.local.contract.RoomContract
import by.bashlikovvv.data.local.dao.HotelsDao
import by.bashlikovvv.data.local.dao.RoomsDao
import by.bashlikovvv.data.remote.HotelApi
import by.bashlikovvv.data.remote.ReservationApi
import by.bashlikovvv.data.remote.RoomsApi
import by.bashlikovvv.data.repository.HotelRepository
import by.bashlikovvv.data.repository.ReservationRepository
import by.bashlikovvv.data.repository.RoomsRepository
import by.bashlikovvv.domain.repository.IHotelRepository
import by.bashlikovvv.domain.repository.IReservationRepository
import by.bashlikovvv.domain.repository.IRoomsRepository
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

    single<HotelReservationDatabase> {
        Room.databaseBuilder(
            context = get(),
            klass = HotelReservationDatabase::class.java,
            name = RoomContract.DATABASE_NAME
        ).build()
    }

    single<HotelsDao> {
        val db: HotelReservationDatabase = get()

        db.hotelsDao
    }

    single<RoomsDao> {
        val db: HotelReservationDatabase = get()

        db.roomsDao
    }

    single<IHotelRepository> {
        val context: Context = get()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        HotelRepository(
            cm = connectivityManager,
            hotelApi = get(),
            hotelsDao = get()
        )
    }

    single<IRoomsRepository> {
        val context: Context = get()
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        RoomsRepository(
            cm = connectivityManager,
            roomsApi = get(),
            roomsDao = get()
        )
    }

    single<IReservationRepository> {
        val reservationApi: ReservationApi = get()

        ReservationRepository(reservationApi)
    }
}