package by.bashlikovvv.hotelreservation.domain.repository

import by.bashlikovvv.hotelreservation.domain.model.Rooms

interface IRoomsRepository {

    suspend fun getRooms(): Rooms
}