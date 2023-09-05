package by.bashlikovvv.hotelreservation.domain.model

data class TouristInfo(
    var name: String = "",
    var inputName: String = "",
    var surname: String = "",
    var dateOfBirth: String = "",
    var citizenship: String = "",
    var passportNumber: String = "",
    var validityPeriod: String = "",
    override val id: Long = 0
): Item(id) {
    fun isEmpty(): Boolean {
        return name.isBlank()
    }
}