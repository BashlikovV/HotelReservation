package by.bashlikovvv.hotelreservation.domain.model

class Description(
    val description: String = "",
    val peculiarities: List<String> = emptyList(),
    id: Long = 0
) : HotelItem(id)