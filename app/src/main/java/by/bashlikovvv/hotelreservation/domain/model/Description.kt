package by.bashlikovvv.hotelreservation.domain.model

data class Description(
    val description: String = "",
    val peculiarities: List<String> = emptyList()
)