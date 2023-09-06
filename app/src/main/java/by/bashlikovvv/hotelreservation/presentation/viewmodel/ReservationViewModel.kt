package by.bashlikovvv.hotelreservation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.model.TouristInfo
import by.bashlikovvv.domain.usecase.GetReservationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReservationViewModel(
    getReservationUseCase: GetReservationUseCase
) : ViewModel() {

    private var _reservation = MutableStateFlow(Reservation())
    val reservation = _reservation.asStateFlow()

    private var _tourists = MutableStateFlow(listOf(TouristInfo(id = 0)))
    val tourists = _tourists.asStateFlow()

    private var _updateVisibility = MutableStateFlow(HotelFragmentViewModel.OnChange(true))
    val updateVisibility = _updateVisibility.asStateFlow()

    init {
        viewModelScope.launch {
            _reservation.update { getReservationUseCase.execute() }
        }.invokeOnCompletion { setUpdateVisibility(false) }
    }

    fun addTourist(touristInfo: TouristInfo) {
        if (touristInfo.isEmpty()) return
        val newList = _tourists.value.toMutableList()
        newList.add(touristInfo)
        _tourists.update { newList }
    }

    fun updateFirsTouristName(name: String) {
        val newList = _tourists.value
        newList[0].name = name
        _tourists.update { newList }
    }

    fun updateTourist(newValue: TouristInfo) {
        val newList = _tourists.value
        newList.onEach {
            if (it.name == newValue.name) {
                it.inputName = newValue.inputName
                it.surname = newValue.surname
                it.citizenship = newValue.citizenship
                it.dateOfBirth = newValue.dateOfBirth
                it.passportNumber = newValue.passportNumber
                it.validityPeriod = newValue.validityPeriod
            }
        }
        _tourists.update { newList }
    }

    fun getLastTouristId() = _tourists.value.last().id

    fun checkTourists(): Long {
        var idx = TOURIST_NOT_FOUND
        val newList = mutableListOf<TouristInfo>()
        _tourists.value.forEach {
            if (!it.isCorrect()) {
                idx = it.id
                newList.add(it.copy(hasError = true))
            } else {
                if (it.hasError) {
                    newList.add(it.copy(hasError = false))
                } else {
                    newList.add(it)
                }
            }
            _tourists.update { newList }
        }

        return idx
    }

    private fun setUpdateVisibility(value: Boolean) {
        _updateVisibility.update { HotelFragmentViewModel.OnChange(value) }
    }

    companion object {
        const val TOURIST_NOT_FOUND = -1L
    }
}