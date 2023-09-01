package by.bashlikovvv.hotelreservation.data.remote.response

import com.google.gson.annotations.SerializedName

data class RoomsDto(
    @SerializedName("rooms") val rooms: List<RoomsItem?>
)