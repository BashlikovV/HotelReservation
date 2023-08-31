package by.bashlikovvv.hotelreservation.data.mapper

import by.bashlikovvv.hotelreservation.data.remote.response.HotelDto
import by.bashlikovvv.hotelreservation.domain.model.Hotel

class HotelDtoMapper : Mapper<HotelDto, Hotel> {

    private val aboutTheHotelDtoMapper = AboutTheHotelDtoMapper()

    override fun mapFromEntity(entity: HotelDto): Hotel {
        return Hotel(
            id = entity.id,
            name = entity.name,
            address = entity.adress,
            minimalPrice = entity.minimalPrice,
            priceForIt = entity.priceForIt,
            rating = entity.rating,
            imagesUrls = entity.imageUrls.filterNotNull(),
            ratingName = entity.ratingName,
            description = aboutTheHotelDtoMapper.mapFromEntity(entity.aboutTheHotelDto)
        )
    }

    override fun mapToEntity(domain: Hotel): HotelDto {
        TODO("Not yet implemented")
    }
}