package by.bashlikovvv.hotelreservation.domain.repository

import by.bashlikovvv.hotelreservation.domain.model.Hotel

interface IHotelRepository {

    suspend fun getHotelInfo(id: Long): Hotel
}