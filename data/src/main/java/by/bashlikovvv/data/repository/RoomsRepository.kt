package by.bashlikovvv.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.data.CommonException
import by.bashlikovvv.data.local.dao.RoomsDao
import by.bashlikovvv.data.mapper.RoomEntityMapper
import by.bashlikovvv.data.mapper.RoomsDtoMapper
import by.bashlikovvv.data.remote.RoomsApi
import by.bashlikovvv.domain.model.Rooms
import by.bashlikovvv.domain.repository.IRoomsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws
import by.bashlikovvv.data.remote.response.RoomsDto
import java.io.IOException

class RoomsRepository(
    private val cm: ConnectivityManager?,
    private val roomsApi: RoomsApi,
    private val roomsDao: RoomsDao
) : IRoomsRepository {

    /**
     * @throws [CommonException.RoomsNotFoundException] when [RoomsDto] is null
     * */
    @Throws(CommonException.RoomsNotFoundException::class)
    override suspend fun getRooms(): Rooms = withContext(Dispatchers.IO) {
        return@withContext if (isConnected()) {
            getRoomsOnline()
        } else {
            getRoomsOffline()
        }
    }

    /**
    * @throws [CommonException.RoomsNotFoundException] when [RoomsDto] is null
    * */
    @Throws(CommonException.RoomsNotFoundException::class)
    private suspend fun getRoomsOnline(): Rooms {
        val roomsDtoManager = RoomsDtoMapper()

        return try {
            val roomsResponse = roomsApi.gerRooms()
            val roomsDto = roomsResponse.body()

            val roomsEntityMapper = RoomEntityMapper()

            val rooms = roomsDtoManager.mapFromEntity(roomsDto ?: throw CommonException.RoomsNotFoundException())
            roomsDao.insertRooms(rooms.rooms.map { roomsEntityMapper.mapToEntity(it) })

            rooms
        } catch (e: CommonException.RoomsNotFoundException) {
            throw CommonException.RoomsNotFoundException()
        } catch (e: IOException) {
            throw CommonException.RoomsNotFoundException(e.cause ?: Throwable(e.message))
        }
    }

    private fun getRoomsOffline(): Rooms {
        val roomEntities = roomsDao.selectRooms()
        val roomsEntityMapper = RoomEntityMapper()

        return if (roomEntities == null) {
            Rooms()
        } else {
            Rooms(rooms = roomEntities.filterNotNull().map { roomsEntityMapper.mapFromEntity(it) })
        }
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