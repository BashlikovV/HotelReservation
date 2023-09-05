package by.bashlikovvv.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.data.mapper.RoomsDtoMapper
import by.bashlikovvv.data.remote.RoomsApi
import by.bashlikovvv.domain.model.Rooms
import by.bashlikovvv.domain.repository.IRoomsRepository

class RoomsRepository(
    private val cm: ConnectivityManager?,
    private val roomsApi: RoomsApi
) : IRoomsRepository {

    override suspend fun getRooms(): Rooms {
        return if (isConnected()) {
            getRoomsOnline()
        } else {
            getRoomsOffline()
        }
    }

    private suspend fun getRoomsOnline(): Rooms {
        val roomsDtoManager = RoomsDtoMapper()
        val roomsResponse = roomsApi.gerRooms()
        val roomsDto = roomsResponse.body()!!
        return roomsDtoManager.mapFromEntity(roomsDto)
    }

    private suspend fun getRoomsOffline(): Rooms {
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