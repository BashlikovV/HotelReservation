package by.bashlikovvv.data.remote

import by.bashlikovvv.data.remote.response.RoomsDto
import retrofit2.Response
import retrofit2.http.GET

interface RoomsApi {

    @GET("f9a38183-6f95-43aa-853a-9c83cbb05ecd/")
    suspend fun gerRooms(): Response<RoomsDto>
}