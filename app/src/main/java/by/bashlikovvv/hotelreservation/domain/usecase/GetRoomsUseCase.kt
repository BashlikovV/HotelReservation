package by.bashlikovvv.hotelreservation.domain.usecase

import by.bashlikovvv.hotelreservation.domain.model.Rooms
import by.bashlikovvv.hotelreservation.domain.repository.IRoomsRepository

class GetRoomsUseCase(private val roomsRepository: IRoomsRepository) {

    suspend fun execute(): Rooms {
        return roomsRepository.getRooms()
    }
}