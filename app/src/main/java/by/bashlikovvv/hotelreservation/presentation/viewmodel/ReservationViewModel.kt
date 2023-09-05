package by.bashlikovvv.hotelreservation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.hotelreservation.domain.model.Reservation
import by.bashlikovvv.hotelreservation.domain.model.TouristInfo
import by.bashlikovvv.hotelreservation.domain.usecase.GetReservationUseCase
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

    fun removeTourist(id: Long) {
        val newList = _tourists.value.filter { it.id != id }
        _tourists.update { newList }
    }

    fun getTouristById(id: Long): TouristInfo {
        return _tourists.value.first { it.id == id }
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

    private fun setUpdateVisibility(value: Boolean) {
        _updateVisibility.update { HotelFragmentViewModel.OnChange(value) }
    }
}