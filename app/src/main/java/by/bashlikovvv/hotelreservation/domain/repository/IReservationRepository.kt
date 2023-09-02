package by.bashlikovvv.hotelreservation.domain.repository

import by.bashlikovvv.hotelreservation.domain.model.Reservation

interface IReservationRepository {

    suspend fun getReservation(): Reservation
}