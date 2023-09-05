package by.bashlikovvv.data.remote.response

import com.google.gson.annotations.SerializedName

data class HotelDto(
    @SerializedName("minimal_price") val minimalPrice: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("rating") val rating: Int = 0,
    @SerializedName("image_urls") val imageUrls: List<String?>,
    @SerializedName("adress") val adress: String = "",
    @SerializedName("rating_name") val ratingName: String = "",
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("price_for_it") val priceForIt: String = "",
    @SerializedName("about_the_hotel") val aboutTheHotelDto: AboutTheHotelDto
)