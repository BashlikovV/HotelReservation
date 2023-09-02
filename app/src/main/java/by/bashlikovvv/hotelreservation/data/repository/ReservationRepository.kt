package by.bashlikovvv.hotelreservation.data.repository

import by.bashlikovvv.hotelreservation.data.Exceptions
import by.bashlikovvv.hotelreservation.data.mapper.ReservationDtoMapper
import by.bashlikovvv.hotelreservation.data.remote.ReservationApi
import by.bashlikovvv.hotelreservation.domain.model.Reservation
import by.bashlikovvv.hotelreservation.domain.repository.IReservationRepository
import kotlin.jvm.Throws

class ReservationRepository(
    private val reservationApi: ReservationApi
) : IReservationRepository {

    @Throws(Exceptions.ReservationNotFoundException::class)
    override suspend fun getReservation(): Reservation {
        val reservationDtoMapper = ReservationDtoMapper()
        val reservationApiResponse = reservationApi.getReservation()

        return reservationDtoMapper.mapFromEntity(
            reservationApiResponse.body() ?: throw Exceptions.ReservationNotFoundException()
        )
    }
}