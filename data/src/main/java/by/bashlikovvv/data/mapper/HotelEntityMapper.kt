package by.bashlikovvv.data.mapper

import by.bashlikovvv.data.local.model.HotelsItemEntity
import by.bashlikovvv.domain.model.Description
import by.bashlikovvv.domain.model.Hotel

class HotelEntityMapper : Mapper<HotelsItemEntity, Hotel> {
    override fun mapFromEntity(entity: HotelsItemEntity): Hotel {
        return Hotel(
            id = entity.id,
            name = entity.name,
            minimalPrice = entity.minimalPrice,
            address = entity.address,
            priceForIt = entity.priceForIt,
            rating = entity.rating,
            ratingName = entity.ratingName,
            imagesUrls = entity.imagesUrls,
            description = Description(
                description = entity.description,
                peculiarities = entity.peculiarities
            )
        )
    }

    override fun mapToEntity(domain: Hotel): HotelsItemEntity {
        return HotelsItemEntity(
            id = domain.id,
            name = domain.name,
            minimalPrice = domain.minimalPrice,
            address = domain.address,
            priceForIt = domain.priceForIt,
            rating = domain.rating,
            ratingName = domain.ratingName,
            imagesUrls = domain.imagesUrls,
            description = domain.description.description,
            peculiarities = domain.description.peculiarities
        )
    }
}