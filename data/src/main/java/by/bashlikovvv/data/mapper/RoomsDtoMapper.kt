package by.bashlikovvv.data.mapper

import by.bashlikovvv.data.remote.response.RoomsDto
import by.bashlikovvv.domain.model.Rooms

class RoomsDtoMapper : Mapper<RoomsDto, Rooms> {
    override fun mapFromEntity(entity: RoomsDto): Rooms {
        val roomsItemDtoMapper = RoomsItemDtoMapper()

        return Rooms(entity.rooms.filterNotNull().map { roomsItem ->
            roomsItemDtoMapper.mapFromEntity(roomsItem)
        })
    }

    override fun mapToEntity(domain: Rooms): RoomsDto {
        throw UnsupportedOperationException("Not implemented")
    }
}