package by.bashlikovvv.domain.usecase

import by.bashlikovvv.domain.model.Rooms
import by.bashlikovvv.domain.repository.IRoomsRepository

class GetRoomsUseCase(private val roomsRepository: IRoomsRepository) {

    suspend fun execute(): Rooms {
        return roomsRepository.getRooms()
    }
}