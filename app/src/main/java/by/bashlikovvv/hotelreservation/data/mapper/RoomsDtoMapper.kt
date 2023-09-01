package by.bashlikovvv.hotelreservation.data.mapper

import by.bashlikovvv.hotelreservation.data.remote.response.RoomsDto
import by.bashlikovvv.hotelreservation.domain.model.Rooms

class RoomsDtoMapper : Mapper<RoomsDto, Rooms> {
    override fun mapFromEntity(entity: RoomsDto): Rooms {
        val roomsItemMapper = RoomsItemMapper()

        return Rooms(entity.rooms.filterNotNull().map { roomsItem ->
            roomsItemMapper.mapFromEntity(roomsItem)
        })
    }

    override fun mapToEntity(domain: Rooms): RoomsDto {
        throw UnsupportedOperationException("Not implemented")
    }
}