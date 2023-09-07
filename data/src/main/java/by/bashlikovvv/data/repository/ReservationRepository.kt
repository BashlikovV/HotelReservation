package by.bashlikovvv.data.repository

import by.bashlikovvv.data.CommonException
import by.bashlikovvv.data.mapper.ReservationDtoMapper
import by.bashlikovvv.data.remote.ReservationApi
import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.repository.IReservationRepository
import kotlin.jvm.Throws
import by.bashlikovvv.data.remote.response.ReservationDto
import java.io.IOException

class ReservationRepository(
    private val reservationApi: ReservationApi
) : IReservationRepository {

    /**
     * @throws CommonException.ReservationNotFoundException when [ReservationDto] is null
     * */
    @Throws(CommonException.ReservationNotFoundException::class)
    override suspend fun getReservation(): Reservation {
        val reservationDtoMapper = ReservationDtoMapper()

        return try {
            val reservationApiResponse = reservationApi.getReservation()

            reservationDtoMapper.mapFromEntity(
                reservationApiResponse.body() ?: throw CommonException.ReservationNotFoundException()
            )
        } catch (e: CommonException.ReservationNotFoundException) {
            throw CommonException.ReservationNotFoundException()
        } catch (e: IOException) {
            throw CommonException.ReservationNotFoundException(e.cause ?: Throwable(e.message))
        }
    }
}