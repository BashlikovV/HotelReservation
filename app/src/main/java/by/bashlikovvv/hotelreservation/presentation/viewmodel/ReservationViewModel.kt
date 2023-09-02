package by.bashlikovvv.hotelreservation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.hotelreservation.domain.model.Reservation
import by.bashlikovvv.hotelreservation.domain.usecase.GetReservationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(
    getReservationUseCase: GetReservationUseCase
) : ViewModel() {

    private var _reservation = MutableStateFlow(Reservation())
    val reservation = _reservation.asStateFlow()

    init {
        viewModelScope.launch {
            _reservation.update { getReservationUseCase.execute() }
        }
    }
}