package by.bashlikovvv.hotelreservation.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.hotelreservation.data.HotelNotFoundException
import by.bashlikovvv.hotelreservation.data.mapper.HotelDtoMapper
import by.bashlikovvv.hotelreservation.data.remote.HotelApi
import by.bashlikovvv.hotelreservation.data.remote.response.HotelDto
import by.bashlikovvv.hotelreservation.domain.model.Hotel
import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository
import kotlin.jvm.Throws

class HotelRepository(
    private val cm: ConnectivityManager?,
    private val hotelApi: HotelApi
) : IHotelRepository {

    /**
     * @id - an assumed id identifying the hotel
     * @throws HotelNotFoundException when [HotelDto] is null
     * */
    @Throws(HotelNotFoundException::class)
    override suspend fun getHotelInfo(id: Long): Hotel {
        return if (isConnected()) {
            getHotelInfoOnline(id)
        } else {
            getHotelInfoOffline(id)
        }
    }

    /**
     * @id - an assumed id identifying the hotel
     * @throws HotelNotFoundException when [HotelDto] is null
     * */
    @Throws(HotelNotFoundException::class)
    private suspend fun getHotelInfoOnline(id: Long): Hotel {
        val hotelInfoResponse = hotelApi.getHotelById(/**[id]*/)
        val hotelDto = hotelInfoResponse.body()

        return if (hotelDto != null) {
            val hotelDtoMapper = HotelDtoMapper()
            hotelDtoMapper.mapFromEntity(hotelDto)
        } else {
            throw HotelNotFoundException()
        }
    }

    private suspend fun getHotelInfoOffline(id: Long): Hotel {
        TODO()
    }

    private fun isConnected(): Boolean {
        var isConnected = false
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                isConnected = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return isConnected
    }
}