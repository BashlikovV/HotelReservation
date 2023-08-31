package by.bashlikovvv.hotelreservation.domain.usecase

import by.bashlikovvv.hotelreservation.domain.model.Hotel
import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository

class GetHotelByIdUseCase(private val hotelRepository: IHotelRepository) {

    suspend fun execute(id: Long): Hotel {
        return hotelRepository.getHotelInfo(id)
    }
}