package by.bashlikovvv.hotelreservation.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomItem(
    val id: Long = 0L,
    val name: String = "",
    val price: Int = 0,
    val pricePer: String = "",
    val peculiarities: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList()
) : Parcelable