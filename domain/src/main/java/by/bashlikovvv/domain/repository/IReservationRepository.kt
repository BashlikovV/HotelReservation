package by.bashlikovvv.domain.repository

import by.bashlikovvv.domain.model.Reservation

interface IReservationRepository {

    suspend fun getReservation(): Reservation
}