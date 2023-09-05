package by.bashlikovvv.domain.usecase

import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.repository.IReservationRepository

class GetReservationUseCase(private val reservationRepository: IReservationRepository) {

    suspend fun execute(): Reservation {
        return reservationRepository.getReservation()
    }
}