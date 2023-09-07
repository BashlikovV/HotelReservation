package by.bashlikovvv.data.mapper

import by.bashlikovvv.data.local.model.RoomsItemEntity
import by.bashlikovvv.domain.model.RoomItem

class RoomEntityMapper : Mapper<RoomsItemEntity, RoomItem> {
    override fun mapFromEntity(entity: RoomsItemEntity): RoomItem {
        return RoomItem(
            id = entity.id,
            name = entity.name,
            price = entity.price,
            pricePer = entity.pricePer,
            peculiarities = entity.peculiarities,
            imageUrls = entity.imageUrls
        )
    }

    override fun mapToEntity(domain: RoomItem): RoomsItemEntity {
        return RoomsItemEntity(
            id = domain.id,
            name = domain.name,
            price = domain.price,
            pricePer = domain.pricePer,
            peculiarities = domain.peculiarities,
            imageUrls = domain.imageUrls
        )
    }
}