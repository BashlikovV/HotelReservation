package by.bashlikovvv.hotelreservation.data.mapper

import by.bashlikovvv.hotelreservation.data.remote.response.RoomsDto
import by.bashlikovvv.hotelreservation.domain.model.Rooms

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