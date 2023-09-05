package by.bashlikovvv.domain.usecase

import by.bashlikovvv.domain.model.Hotel
import by.bashlikovvv.domain.repository.IHotelRepository

class GetHotelByIdUseCase(private val hotelRepository: IHotelRepository) {

    suspend fun execute(id: Long): Hotel {
        return hotelRepository.getHotelInfo(id)
    }
}