package by.bashlikovvv.hotelreservation.domain.model

data class Hotel(
    override val id: Long = 0L,
    val name: String = "",
    val address: String = "",
    val minimalPrice: Int = 0,
    val priceForIt: String = "",
    val rating: Int = 0,
    val ratingName: String = "",
    val imagesUrls: List<String> = emptyList(),
    val description: Description = Description()
) : Item(id) {

    fun isEmpty() = name.isEmpty()
}