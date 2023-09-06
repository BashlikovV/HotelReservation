package by.bashlikovvv.domain.model

data class TouristInfo(
    var name: String = "",
    var inputName: String = "",
    var surname: String = "",
    var dateOfBirth: String = "",
    var citizenship: String = "",
    var passportNumber: String = "",
    var validityPeriod: String = "",
    var hasError: Boolean = false,
    override val id: Long = 0
): Item(id) {
    fun isEmpty(): Boolean {
        return name.isBlank()
    }

    fun isCorrect(): Boolean {
        if (inputName.isBlank()) return false
        if (inputName.isBlank()) return false
        if (surname.isBlank()) return false
        if (dateOfBirth.isBlank()) return false
        if (citizenship.isBlank()) return false
        if (passportNumber.isBlank()) return false

        return validityPeriod.isNotBlank()
    }
}