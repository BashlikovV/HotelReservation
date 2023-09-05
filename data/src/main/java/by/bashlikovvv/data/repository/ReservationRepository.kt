package by.bashlikovvv.data.repository

import by.bashlikovvv.data.Exceptions
import by.bashlikovvv.data.mapper.ReservationDtoMapper
import by.bashlikovvv.data.remote.ReservationApi
import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.repository.IReservationRepository
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