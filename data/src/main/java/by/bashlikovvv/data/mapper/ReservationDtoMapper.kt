package by.bashlikovvv.data.mapper

import by.bashlikovvv.data.remote.response.ReservationDto
import by.bashlikovvv.domain.model.Reservation

class ReservationDtoMapper : Mapper<ReservationDto, Reservation> {

    override fun mapFromEntity(entity: ReservationDto): Reservation {
        return Reservation(
            hotelAddress = entity.hotelAdress,
            rating = entity.horating,
            fuelCharge = entity.fuelCharge,
            ratingName = entity.ratingName,
            tourDateStart = entity.tourDateStart,
            room = entity.room,
            tourDateStop = entity.tourDateStop,
            hotelName = entity.hotelName,
            arrivalCountry = entity.arrivalCountry,
            numberOfNights = entity.numberOfNights,
            nutrition = entity.nutrition,
            serviceCharge = entity.serviceCharge,
            id = entity.id,
            departure = entity.departure,
            tourPrice = entity.tourPrice
        )
    }

    override fun mapToEntity(domain: Reservation): ReservationDto {
        throw UnsupportedOperationException("not implemented")
    }
}