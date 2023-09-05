package by.bashlikovvv.data.mapper

import by.bashlikovvv.data.remote.response.RoomsItemDto
import by.bashlikovvv.domain.model.RoomItem

class RoomsItemDtoMapper : Mapper<RoomsItemDto, RoomItem> {

    override fun mapFromEntity(entity: RoomsItemDto): RoomItem {
        return RoomItem(
            id = entity.id,
            name = entity.name,
            price = entity.price,
            pricePer = entity.pricePer,
            peculiarities = entity.peculiarities.filterNotNull(),
            imageUrls = entity.imageUrls.filterNotNull()
        )
    }

    override fun mapToEntity(domain: RoomItem): RoomsItemDto {
        throw UnsupportedOperationException("Not implemented")
    }
}