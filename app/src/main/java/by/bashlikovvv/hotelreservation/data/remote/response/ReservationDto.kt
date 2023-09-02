package by.bashlikovvv.hotelreservation.data.remote.response

import com.google.gson.annotations.SerializedName

data class ReservationDto(
    @SerializedName("hotel_adress") val hotelAdress: String = "",
    @SerializedName("horating") val horating: Int = 0,
    @SerializedName("fuel_charge") val fuelCharge: Int = 0,
    @SerializedName("rating_name") val ratingName: String = "",
    @SerializedName("tour_date_start") val tourDateStart: String = "",
    @SerializedName("tour_date_stop") val tourDateStop: String = "",
    @SerializedName("room") val room: String = "",
    @SerializedName("hotel_name") val hotelName: String = "",
    @SerializedName("arrival_country") val arrivalCountry: String = "",
    @SerializedName("number_of_nights") val numberOfNights: Int = 0,
    @SerializedName("nutrition") val nutrition: String = "",
    @SerializedName("service_charge") val serviceCharge: Int = 0,
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("departure") val departure: String = "",
    @SerializedName("tour_price") val tourPrice: Int = 0
)