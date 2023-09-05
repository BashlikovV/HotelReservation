package by.bashlikovvv.data.mapper

import by.bashlikovvv.data.remote.response.AboutTheHotelDto
import by.bashlikovvv.domain.model.Description

class AboutTheHotelDtoMapper : Mapper<AboutTheHotelDto, Description> {

    override fun mapFromEntity(entity: AboutTheHotelDto): Description {
        return Description(
            description = entity.description,
            peculiarities = entity.peculiarities.filterNotNull()
        )
    }

    override fun mapToEntity(domain: Description): AboutTheHotelDto {
        throw UnsupportedOperationException("Not implemented")
    }
}