package by.bashlikovvv.hotelreservation.domain.usecase

import by.bashlikovvv.hotelreservation.domain.model.Reservation
import by.bashlikovvv.hotelreservation.domain.repository.IReservationRepository

class GetReservationUseCase(private val reservationRepository: IReservationRepository) {

    suspend fun execute(): Reservation {
        return reservationRepository.getReservation()
    }
}