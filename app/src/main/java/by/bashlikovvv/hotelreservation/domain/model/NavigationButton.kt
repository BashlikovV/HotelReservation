package by.bashlikovvv.hotelreservation.domain.model

class NavigationButton(
    override val id: Long = 0L,
    val hotel: Hotel
) : Item()