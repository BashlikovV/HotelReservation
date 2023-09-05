package by.bashlikovvv.domain.repository

import by.bashlikovvv.domain.model.Hotel

interface IHotelRepository {

    suspend fun getHotelInfo(id: Long): Hotel
}