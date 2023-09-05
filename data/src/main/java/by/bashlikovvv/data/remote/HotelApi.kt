package by.bashlikovvv.data.remote

import by.bashlikovvv.data.remote.response.HotelDto
import retrofit2.Response
import retrofit2.http.GET

interface HotelApi {

    @GET(/*hotels/{id}*/"35e0d18e-2521-4f1b-a575-f0fe366f66e3/")
    suspend fun getHotelById(
        /*@Path("id") id: Long*/
    ): Response<HotelDto>
}