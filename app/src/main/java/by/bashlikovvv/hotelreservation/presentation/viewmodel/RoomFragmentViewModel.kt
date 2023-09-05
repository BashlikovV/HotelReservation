package by.bashlikovvv.hotelreservation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.hotelreservation.domain.model.Rooms
import by.bashlikovvv.hotelreservation.domain.usecase.GetRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomFragmentViewModel(
    getRoomsUseCase: GetRoomsUseCase
) : ViewModel() {

    private var _updateVisibility: MutableStateFlow<Boolean> = MutableStateFlow(
        false
    )
    val updateVisibility = _updateVisibility.asStateFlow()

    private var _rooms = MutableStateFlow(Rooms())
    val rooms = _rooms.asStateFlow()

    init {
        _updateVisibility.update { true }
        viewModelScope.launch {
            _rooms.update { getRoomsUseCase.execute() }
        }.invokeOnCompletion {
            _updateVisibility.update { false }
        }
    }
}
