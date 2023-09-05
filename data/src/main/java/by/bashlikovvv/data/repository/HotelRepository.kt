package by.bashlikovvv.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.data.Exceptions
import by.bashlikovvv.data.mapper.HotelDtoMapper
import by.bashlikovvv.data.remote.HotelApi
import by.bashlikovvv.data.remote.response.HotelDto
import by.bashlikovvv.domain.model.Hotel
import by.bashlikovvv.domain.repository.IHotelRepository
import kotlin.jvm.Throws

class HotelRepository(
    private val cm: ConnectivityManager?,
    private val hotelApi: HotelApi
) : IHotelRepository {

    /**
     * @id - an assumed id identifying the hotel
     * @throws Exceptions.HotelNotFoundException when [HotelDto] is null
     * */
    @Throws(Exceptions.HotelNotFoundException::class)
    override suspend fun getHotelInfo(id: Long): Hotel {
        return if (isConnected()) {
            getHotelInfoOnline(id)
        } else {
            getHotelInfoOffline(id)
        }
    }

    /**
     * @id - an assumed id identifying the hotel
     * @throws Exceptions.HotelNotFoundException when [HotelDto] is null
     * */
    @Throws(Exceptions.HotelNotFoundException::class)
    private suspend fun getHotelInfoOnline(id: Long): Hotel {
        val hotelInfoResponse = hotelApi.getHotelById(/**[id]*/)
        val hotelDto = hotelInfoResponse.body()

        return if (hotelDto != null) {
            val hotelDtoMapper = HotelDtoMapper()
            hotelDtoMapper.mapFromEntity(hotelDto)
        } else {
            throw Exceptions.HotelNotFoundException()
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