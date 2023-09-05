package by.bashlikovvv.data.remote

import by.bashlikovvv.data.remote.response.ReservationDto
import retrofit2.Response
import retrofit2.http.GET

interface ReservationApi {

    @GET("e8868481-743f-4eb2-a0d7-2bc4012275c8/")
    suspend fun getReservation(): Response<ReservationDto>
}