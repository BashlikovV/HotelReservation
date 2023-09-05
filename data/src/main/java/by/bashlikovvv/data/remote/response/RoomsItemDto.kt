package by.bashlikovvv.data.remote.response

import com.google.gson.annotations.SerializedName

data class RoomsItemDto(
    @SerializedName("price") val price: Int = 0,
    @SerializedName("price_per") val pricePer: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("image_urls") val imageUrls: List<String?>,
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("peculiarities") val peculiarities: List<String?>
)