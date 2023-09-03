package by.bashlikovvv.hotelreservation.domain.model

data class TouristInfo(
    var name: String = "",
    var inputName: String = "",
    var surname: String = "",
    var dateOfBirth: String = "",
    var citizenship: String = "",
    var passportNumber: String = "",
    var validityPeriod: String = "",
    val id: Int = 0
) {
    fun isEmpty(): Boolean {
        return name.isBlank()
    }
}