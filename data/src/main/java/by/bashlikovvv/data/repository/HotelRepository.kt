package by.bashlikovvv.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.data.CommonException
import by.bashlikovvv.data.local.dao.HotelsDao
import by.bashlikovvv.data.local.model.HotelsItemEntity
import by.bashlikovvv.data.mapper.HotelDtoMapper
import by.bashlikovvv.data.mapper.HotelEntityMapper
import by.bashlikovvv.data.remote.HotelApi
import by.bashlikovvv.data.remote.response.HotelDto
import by.bashlikovvv.domain.model.Hotel
import by.bashlikovvv.domain.repository.IHotelRepository
import java.io.IOException
import kotlin.jvm.Throws

class HotelRepository(
    private val cm: ConnectivityManager?,
    private val hotelApi: HotelApi,
    private val hotelsDao: HotelsDao
) : IHotelRepository {
    /**
     * @id - an assumed id identifying the hotel
     * @throws CommonException.HotelNotFoundException when [HotelDto] is null
     * */
    @Throws(CommonException.HotelNotFoundException::class)
    override suspend fun getHotelInfo(id: Long): Hotel {
        return if (isConnected()) {
            getHotelInfoOnline(id)
        } else {
            getHotelInfoOffline(id)
        }
    }

    /**
     * @id - an assumed id identifying the hotel
     * @throws CommonException.HotelNotFoundException when [HotelDto] is null
     * */
    @Throws(CommonException.HotelNotFoundException::class)
    private suspend fun getHotelInfoOnline(id: Long): Hotel {

        return try {
            val hotelInfoResponse = hotelApi.getHotelById(/**[id]*/)
            val hotelDto = hotelInfoResponse.body()

            if (hotelDto != null) {
                val hotelDtoMapper = HotelDtoMapper()
                val hotelEntityMapper = HotelEntityMapper()

                val hotel = hotelDtoMapper.mapFromEntity(hotelDto)
                hotelsDao.insertHotels(listOf(hotelEntityMapper.mapToEntity(hotel)))

                hotel
            } else {
                throw CommonException.HotelNotFoundException()
            }
        } catch (e: CommonException.HotelNotFoundException) {
            throw CommonException.HotelNotFoundException()
        } catch (e: IOException) {
            throw CommonException.HotelNotFoundException(e.cause ?: Throwable(e.message))
        }
    }

    /**
     * @id - an assumed id identifying the hotel
     * @throws CommonException.HotelNotFoundException when [HotelsItemEntity] is null
     * */
    private fun getHotelInfoOffline(id: Long): Hotel {
        val hotelEntity = hotelsDao.selectHotelById(id + 1)
        val hotelEntityMapper = HotelEntityMapper()

        return hotelEntityMapper.mapFromEntity(hotelEntity ?: throw CommonException.HotelNotFoundException())
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