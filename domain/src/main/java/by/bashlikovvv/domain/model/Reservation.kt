package by.bashlikovvv.domain.model

data class Reservation(
    val hotelAddress: String = "",
    val rating: Int = 0,
    val fuelCharge: Int = 0,
    val ratingName: String = "",
    val tourDateStart: String = "",
    val tourDateStop: String = "",
    val room: String = "",
    val hotelName: String = "",
    val arrivalCountry: String = "",
    val numberOfNights: Int = 0,
    val nutrition: String = "",
    val serviceCharge: Int = 0,
    val id: Long = 0L,
    val departure: String = "",
    val tourPrice: Int = 0
)