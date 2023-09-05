package by.bashlikovvv.domain.repository

import by.bashlikovvv.domain.model.Rooms

interface IRoomsRepository {

    suspend fun getRooms(): Rooms
}